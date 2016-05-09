package BackEnd;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class to hold a binary search tree (by date)
 *  of weather observations.
 */
public class BST implements Serializable {
    private Node root;

    public BST() {
        root = null;
    }

    public void add(WeatherObservation o) {
        if (root == null) {
            root = new Node(o);
        } else {
            root.insert(o);
        }
    }

    public Integer size() {
        if (root == null) {
            return 0;
        } else {
            return root.size();
        }
    }

    public ArrayList<WeatherObservation> find(Date d) {
        if (root == null) {
            return new ArrayList<>();
        } else {
            return root.find(d);
        }
    }

    public ArrayList<WeatherObservation> traverse() {
        if (root == null) {
            return new ArrayList<>();
        } else {
            return root.traverse();
        }
    }

    @Override
    public String toString() {
        if (root == null) {
            return "digraph BST {\n\n}\n"; // empty graph but still valid DOT
        } else {
            return root.toString();
        }
    }

    private class Node implements Serializable {

        private ArrayList<WeatherObservation> obs = new ArrayList<>();
        private final Date date;
        private Node leftChild = null;
        private Node rightChild = null;

        public Node(WeatherObservation o) {
            obs.add(o);
            this.date = o.getDate();
        }

        public void insert(WeatherObservation o) {
            if (o.getDate().compareTo(date) == 0) {
                obs.add(o);
            } else if (o.getDate().compareTo(date) > 0) {
                if (rightChild == null) {
                    rightChild = new Node(o);
                } else {
                    rightChild.insert(o);
                }
            } else {
                if (leftChild == null) {
                    leftChild = new Node(o);
                } else {
                    leftChild.insert(o);
                }
            }
        }

        // this tostring method will return a string containing the binary tree structure
        //  in graphviz DOT language (http://graphviz.org/)
        // - to visualize the tree, run the string through the 'dot' command.
        @Override
        public String toString() {
            String s = "digraph BST {\n";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            if (leftChild == null && rightChild == null) {
                s += "  \"" + format.format(date) + "\";\n";
            }
            if (leftChild != null) {
                s += "  \"" + format.format(date) + "\" -> \"" + format.format(leftChild.date) + "\";\n";
                s += leftChild.toStringHelper();
            }
            if (rightChild != null) {
                s += "  \"" + format.format(date) + "\" -> \"" + format.format(rightChild.date) + "\";\n";
                s += rightChild.toStringHelper();
            }
            s += "}\n";
            return s;

        }

        // helper for the tostring method
        private String toStringHelper() {
            String s = "";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            if (leftChild != null) {
                s += "  \"" + format.format(date) + "\" -> \"" + format.format(leftChild.date) + "\";\n";
                s += leftChild.toStringHelper();
            }
            if (rightChild != null) {
                s += "  \"" + format.format(date) + "\" -> \"" + format.format(rightChild.date) + "\";\n";
                s += rightChild.toStringHelper();
            }
            return s;
        }

        public Integer size() {
            Integer sum = 1;
            if (leftChild != null) {
                sum += leftChild.size();
            }
            if (rightChild != null) {
                sum += rightChild.size();
            }
            return sum;
        }

        public ArrayList<WeatherObservation> find(Date d) {
            if (d.compareTo(date) == 0) {
                return obs;
            } else if (d.compareTo(date) > 0 && rightChild != null) {
                return rightChild.find(d);
            } else if (d.compareTo(date) < 0 && leftChild != null) {
                return leftChild.find(d);
            } else {
                return new ArrayList<>();
            }
        }

        public ArrayList<WeatherObservation> traverse() {
            ArrayList<WeatherObservation> a = new ArrayList<>();

            if (leftChild != null) {
                a.addAll(leftChild.traverse());
            }

            a.addAll(obs);

            if (rightChild != null) {
                a.addAll(rightChild.traverse());
            }

            return a;
        }
    }

}

