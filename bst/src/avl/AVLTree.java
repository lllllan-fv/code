package avl;

@SuppressWarnings({"all"})
public class AVLTree<T extends Comparable<T>> {

    private AVLNode<T> root;

    public AVLTree() {
    }

    public AVLTree(AVLNode<T> root) {
        this.root = root;
    }

    /**
     * 查找
     *
     * @param value
     * @return
     */
    public AVLNode<T> search(T value) {
        return search(root, value);
    }

    public AVLNode<T> search(AVLNode<T> node, T value) {
        if (root == null || value == null) return null;
        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return search(node.left, value);
        } else {
            return search(node.right, value);
        }
    }

    /**
     * 找最大值
     *
     * @return
     */
    public T searchMaxValue() {
        if (root == null) return null;

        AVLNode<T> node = searchMaxValue(root);
        return node == null ? null : node.value;
    }

    public AVLNode<T> searchMaxValue(AVLNode<T> node) {
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

        AVLNode<T> node = searchMinValue(root);
        return node == null ? null : node.value;
    }

    public AVLNode<T> searchMinValue(AVLNode<T> node) {
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
            root = new AVLNode<>(value, null, null);
            return true;
        }

        return insert(null, root, value);
    }

    public boolean insert(AVLNode<T> pre, AVLNode<T> node, T value) {
        boolean res;

        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            res = false;
        } else if (cmp < 0) {
            if (node.left == null) {
                node.left = new AVLNode(value, null, null);
                res = true;
            } else {
                res = insert(node, node.left, value);
            }
        } else {
            if (node.right == null) {
                node.right = new AVLNode(value, null, null);
                res = true;
            } else {
                res = insert(node, node.right, value);
            }
        }

        rebalance(pre, node);

        return res;
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

    public boolean delete(AVLNode<T> pre, AVLNode<T> node, T value) {
        if (node == null) return false;
        boolean res;

        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            delete(pre, node);
            res = true;
        } else if (cmp < 0) {
            res = delete(node, node.left, value);
        } else {
            res = delete(node, node.right, value);
        }

        if (pre == null) {
            root = node;
        } else if (pre.left == node) {
            pre.left = node;
        } else {
            pre.right = node;
        }

        rebalance(pre, node);

        return res;
    }

    public void delete(AVLNode<T> pre, AVLNode<T> node) {
        AVLNode ori = node;
        if (node.right == null) {
            // 右子树为空，直接接上左子树
            node = node.left;
        } else if (node.left == null) {
            // 左子树为空，直接接上右子树
            node = node.right;
        } else {
            // 都不为空
            // 找到左子树中，最大的节点，并替换到删除节点位置处
            AVLNode<T> parent = node;
            AVLNode<T> leftMaxValueNode = node.left;
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

        if (pre == null) {
            root = node;
        } else if (ori == pre.left) {
            pre.left = node;
        } else {
            pre.right = node;
        }

        // 平衡左右子树，再平衡自己
        rebalance(node, node.left);
        rebalance(node, node.right);
        rebalance(pre, node);

    }

    /**
     * 平衡方法
     *
     * @param node
     */
    public void rebalance(AVLNode<T> pre, AVLNode<T> node) {
        if (node == null) return;

        node.updateHeight();
        int balance = node.balanceFactor();
        if (balance >= -1 && balance <= 1) return;

        AVLNode link;
        if (balance == -2) {
            AVLNode right = node.right;
            node.right = right.left;
            right.left = node;
            link = right;
        } else {
            AVLNode left = node.left;
            node.left = left.right;
            left.right = node;
            link = left;
        }

        node.updateHeight();
        link.updateHeight();

        if (pre == null) {
            root = link;
        } else if (pre.left == node) {
            pre.left = link;
        } else {
            pre.right = link;
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

    public StringBuilder preOrder(AVLNode<T> node) {
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

    public StringBuilder inOrder(AVLNode<T> node) {
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

    public StringBuilder postOrder(AVLNode<T> node) {
        if (node == null) return new StringBuilder("");

        StringBuilder res = new StringBuilder(postOrder(node.left));
        res.append(postOrder(node.right));
        res.append(node.value.toString()).append(",");

        return res;
    }

    public void printEdge() {
        printEdge(root);
    }

    public void printEdge(AVLNode<T> node) {
        if (node == null) return;
        if (node.left != null) {
            System.out.println(node.value.toString() + " " + node.left.value.toString());
            printEdge(node.left);
        }
        if (node.right != null) {
            System.out.println(node.value.toString() + " " + node.right.value.toString());
            printEdge(node.right);
        }
    }

    public class AVLNode<T extends Comparable<T>> {
        T value;
        int height;
        AVLNode<T> left;
        AVLNode<T> right;

        public AVLNode(T value, AVLNode<T> left, AVLNode<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.height = 1;
        }

        public int balanceFactor() {
            return getHeight(left) - getHeight(right);
        }

        public void updateHeight() {
            height = Math.max(getHeight(left), getHeight(right)) + 1;
        }

        public int getHeight(AVLNode<T> node) {
            return node == null ? 0 : node.height;
        }
    }

}
