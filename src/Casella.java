public class Casella<T, E> {
    // Stringa che contiene la parola
    T element;
    // Riferimento ai parent che generano la parola
    E parentOne;
    E parentTwo;

    public Casella(T element) {
        this.element = element;
        parentOne = null;
        parentTwo = null;
    }

    public Casella(T element, E parentOne, E parentTwo) {
        this.element = element;
        this.parentOne = parentOne;
        this.parentTwo = parentTwo;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public E getParentOne() {
        return parentOne;
    }

    public void setParentOne(E parentOne) {
        this.parentOne = parentOne;
    }

    public E getParentTwo() {
        return parentTwo;
    }

    public void setParentTwo(E parentTwo) {
        this.parentTwo = parentTwo;
    }
}