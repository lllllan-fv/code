public class Main {

    public static void main(String[] args) {
        BSTree<Integer> tree = new BSTree<>();

        int[] arr = new int[]{62, 58, 47, 35, 29, 37, 36, 51, 49, 48, 50, 56, 88, 73, 99, 93};

        for (int num : arr) tree.insert(num);

        tree.delete(47);

        tree.printPreOrder();
        tree.printInOrder();
        tree.printPostOrder();
    }

}