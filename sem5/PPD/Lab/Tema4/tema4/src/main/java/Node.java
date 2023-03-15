public class Node {
    private final int exponent;
    private int coefficient;
    private Node next;

    public Node(int exponent, int coefficient) {
        this.exponent = exponent;
        this.coefficient = coefficient;
        this.next = null;
    }

    public int getExponent() {
        return exponent;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Integer coefficient) {
        this.coefficient = coefficient;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
