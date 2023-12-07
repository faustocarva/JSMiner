package br.unb.cic.js.walker;

import br.unb.cic.js.date.Interval;
import br.unb.cic.js.walker.exception.NoBranchFoundException;
import lombok.NoArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;

@NoArgsConstructor
final class RepositoryWalkerGit {
	
    // Custom RevFilter for including only first parent commits and excluding second parent commits
    public static class FirstParentFilter extends RevFilter {
        private Set<RevCommit> ignoreCommits = new HashSet<>();

        @Override
        public boolean include(RevWalk revWalk, RevCommit commit) throws IOException {
            // If the commit has more than one parent (is a merge commit),
            // add the second parent to the list of commits to be ignored.
            if (commit.getParentCount() > 1) {
                ignoreCommits.add(commit.getParent(1));
            }

            // Initialize the 'include' variable as true.
            boolean include = true;

            // If the commit is in the list of commits to be ignored,
            // set 'include' to false and remove the commit from the list.
            if (ignoreCommits.contains(commit)) {
                include = false;
                ignoreCommits.remove(commit);
            }

            // Return whether the commit should be included or not.
            return include;
        }

        @Override
        public RevFilter clone() {
            // Clone the filter for internal use in RevWalk.
            return new FirstParentFilter();
        }
    }


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

    // Method to get a list of commits in linear order from a Git repository
    public static Iterable<RevCommit> revisions(Repository repository) throws Exception {

        try (RevWalk revWalk = new RevWalk(repository)) {

            // Configure sorting for topological order and reverse chronological order
            revWalk.sort(RevSort.TOPO);
            revWalk.sort(RevSort.REVERSE, true);

            // Apply the custom FirstParentFilter to include only first parent commits
            revWalk.setRevFilter(new FirstParentFilter());

            // Get the most recent commit (HEAD)
            // Mark the start points (from the oldest to the most recent)
            RevCommit head = revWalk.parseCommit(repository.resolve("HEAD"));
            revWalk.markStart(head);

            List<RevCommit> revCommits = new ArrayList<>();

            // Iterate over the commits in reverse chronological order
            for (RevCommit commit : revWalk) {
                // If the commit has only one parent, add it to the list
                if (commit.getParentCount() == 1) {
                    revCommits.add(commit);
                }
            }
            
            Iterable<RevCommit> iterableCommits = revCommits::iterator;

            return iterableCommits;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}