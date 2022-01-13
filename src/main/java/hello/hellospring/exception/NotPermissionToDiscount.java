package hello.hellospring.exception;

public class NotPermissionToDiscount extends RuntimeException {

    public NotPermissionToDiscount() {
        super();
    }

    public NotPermissionToDiscount(String message) {
        super(message);
    }
}
