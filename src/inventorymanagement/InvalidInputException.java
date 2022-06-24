package inventorymanagement;
//Used for input validation
public class InvalidInputException extends RuntimeException {
    InvalidInputException(String msg) {
        super(msg);
    }
}
