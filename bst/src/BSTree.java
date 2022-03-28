@SuppressWarnings({"all"})
public class BSTree<T extends Comparable<T>> {

    private BSTNode<T> root;

    public BSTree() {
    }

    public BSTree(BSTNode<T> root) {
        this.root = root;
    }

    /**
     * 查找
     *
     * @param value
     * @return
     */
    public BSTNode<T> search(T value) {
        return search(root, value);
    }

    public BSTNode<T> search(BSTNode<T> node, T value) {
        return null;
    }

    /**
     * 找最大值
     *
     * @return
     */
    public T searchMaxValue() {
        if (root == null) return null;

        BSTNode<T> node = searchMaxValue(root);
        return node == null ? null : node.value;
    }

    public BSTNode<T> searchMaxValue(BSTNode<T> node) {
        while (node.right != null) node = node.right;
        return node;
    }

    /**
     * 找最小值
     *
     * @return
     */
    public T searchMinValue() {
        if (root == null) return null;

        BSTNode<T> node = searchMinValue(root);
        return node == null ? null : node.value;
    }

    public BSTNode<T> searchMinValue(BSTNode<T> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /**
     * 插入节点 （去重）
     *
     * @param value
     * @return 是否插入成功
     */
    public boolean insert(T value) {
        if (value == null) return false;
        if (root == null) {
            root = new BSTNode<>(value, null, null);
            return true;
        }

        return insert(root, value);
    }

    public boolean insert(BSTNode<T> node, T value) {
        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            return false;
        } else if (cmp < 0) {
            if (node.left == null) {
                node.left = new BSTNode(value, null, null);
                return true;
            } else {
                return insert(node.left, value);
            }
        } else {
            if (node.right == null) {
                node.right = new BSTNode(value, null, null);
                return true;
            } else {
                return insert(node.right, value);
            }
        }
    }

    /**
     * 删除节点
     *
     * @param value
     * @return 是否删除成功
     */
    public boolean delete(T value) {
        if (value == null) return false;
        return delete(null, root, value);
    }

    public boolean delete(BSTNode<T> pre, BSTNode<T> node, T value) {
        if (node == null) return false;

        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            delete(pre, node);
            return true;
        } else if (cmp < 0) {
            return delete(node, node.left, value);
        } else {
            return delete(node, node.right, value);
        }
    }

    public void delete(BSTNode<T> pre, BSTNode<T> node) {
        if (node.right == null) {
            // 右子树为空，直接接上左子树
            node = node.left;
        } else if (node.left == null) {
            // 左子树为空，直接接上右子树
            node = node.right;
        } else {
            // 都不为空
            // 找到左子树中，最大的节点，并替换到删除节点位置处
            BSTNode<T> parent = node;
            BSTNode<T> leftMaxValueNode = node.left;
            while (leftMaxValueNode.right != null) {
                parent = leftMaxValueNode;
                leftMaxValueNode = leftMaxValueNode.right;
            }

            node.value = leftMaxValueNode.value;
            if (parent == node) {
                // 如果替换节点，就是删除节点的左节点。将替换节点的左子树，替换删除节点的左子树
                parent.left = leftMaxValueNode.left;
            } else {
                // 否则，将替换节点的左子树，连接到其父亲节点的右子树上
                parent.right = leftMaxValueNode.left;
            }
        }
    }

    /**
     * 打印前序遍历
     */
    public void printPreOrder() {
        StringBuilder str = new StringBuilder("前序遍历：[");
        if (root != null) {
            str.append(preOrder(root));
            str.deleteCharAt(str.length() - 1);
        }
        str.append("]");

        System.out.println(str);
    }

    public StringBuilder preOrder(BSTNode<T> node) {
        if (node == null) return new StringBuilder("");

        StringBuilder res = preOrder(node.left);
        res.append(node.value.toString()).append(",");
        res.append(preOrder(node.right));

        return res;
    }

    /**
     * 打印中序遍历
     */
    public void printInOrder() {
        StringBuilder str = new StringBuilder("中序遍历：[");
        if (root != null) {
            str.append(inOrder(root));
            str.deleteCharAt(str.length() - 1);
        }
        str.append("]");

        System.out.println(str);
    }

    public StringBuilder inOrder(BSTNode<T> node) {
        if (node == null) return new StringBuilder("");

        StringBuilder res = new StringBuilder(node.value.toString()).append(",");
        res.append(inOrder(node.left));
        res.append(inOrder(node.right));

        return res;
    }

    /**
     * 打印后序遍历
     */
    public void printPostOrder() {
        StringBuilder str = new StringBuilder("后序遍历：[");
        if (root != null) {
            str.append(postOrder(root));
            str.deleteCharAt(str.length() - 1);
        }
        str.append("]");

        System.out.println(str);
    }

    public StringBuilder postOrder(BSTNode<T> node) {
        if (node == null) return new StringBuilder("");

        StringBuilder res = new StringBuilder(postOrder(node.left));
        res.append(postOrder(node.right));
        res.append(node.value.toString()).append(",");

        return res;
    }

    public class BSTNode<T extends Comparable<T>> {
        T value;
        BSTNode<T> left;
        BSTNode<T> right;

        public BSTNode(T value, BSTNode<T> left, BSTNode<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

}