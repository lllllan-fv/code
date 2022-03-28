package avl;

public class Main {

    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree();

        for (int i = 0; i < 20; ++i) {
            tree.insert(i);
        }

        tree.printEdge();
    }
}
