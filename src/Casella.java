public class Casella {
    private String element;
    private Casella parentOne;
    private Casella parentTwo;

    public Casella(String element) {
        this.element = element;
        parentOne = null;
        parentTwo = null;
    }

    public Casella(String element, Casella parentOne, Casella parentTwo) {
        this.element = element;
        this.parentOne = parentOne;
        this.parentTwo = parentTwo;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Casella getParentOne() {
        return parentOne;
    }

    public void setParentOne(Casella parentOne) {
        this.parentOne = parentOne;
    }

    public Casella getParentTwo() {
        return parentTwo;
    }

    public void setParentTwo(Casella parentTwo) {
        this.parentTwo = parentTwo;
    }

    @Override
    public String toString() {
        return element;
    }
}