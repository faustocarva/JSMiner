package br.unb.cic.js.walker;

import br.unb.cic.js.date.Interval;
import br.unb.cic.js.walker.exception.NoBranchFoundException;
import lombok.NoArgsConstructor;
import lombok.val;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.revwalk.filter.RevFilter;

@NoArgsConstructor
final class RepositoryWalkerGit {

    public static ObjectId head(final Repository repository) throws Exception {
        var mainBranch = "";

        try (val git = new Git(repository)) {
            val branches = git.branchList()
                    .setListMode(ListBranchCommand.ListMode.REMOTE)
                    .call()
                    .stream()
                    .filter(n -> n.getName().equals("refs/remotes/origin/HEAD"))
                    .findFirst();

            if (branches.isPresent()) {
                mainBranch = branches.get().getTarget().getName().substring("refs/remotes/origin/".length());
            } else {
                throw new NoBranchFoundException();
            }

            git.reset().setMode(ResetCommand.ResetType.HARD).call();
            git.checkout().setName(mainBranch).call();
        }

        return repository.resolve("refs/heads/" + mainBranch);
    }

    public static Iterable<RevCommit> revisions(final Repository repository, final ObjectId head, final Interval interval) throws Exception {
        try (val git = new Git(repository)) {
            return git.log()
                    .add(head)
                    .setRevFilter(CommitTimeRevFilter.between(interval.begin, interval.end))
                    .setRevFilter(RevFilter.NO_MERGES)
                    .call();
        }
    }
}
