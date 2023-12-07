package br.unb.cic.js.walker.exception;

public class NoBranchFoundException extends RuntimeException {

    public NoBranchFoundException() {
        super("the main branch was not found in the git repository");
    }
}
