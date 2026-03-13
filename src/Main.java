import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<ArrayList<Point>> galleries = createGalleries(br);

        for (ArrayList<Point> g : galleries) {
            g = this.grahamScan(g);

            // for (Point p : g) {
            //     System.out.println(p.toString());
            // }

            // System.out.println("----------------------------------");
        }
    }

    private ArrayList<Point> grahamScan(ArrayList<Point> gallery) {
        this.orderGallery(gallery);

        Stack<Point> supSide = new Stack<>();
        supSide.push(gallery.get(0));
        supSide.push(gallery.get(1));

        for (int i = 2; i < gallery.size(); i++) {
            supSide.push(gallery.get(i));
            while ((supSide.size() > 2) && this.CCW(supSide.get(supSide.size() - 3), supSide.get(supSide.size() - 2), supSide.peek())) {
                supSide.remove(supSide.size() - 2);
            }
        }
        // System.out.println("----------------------------------");
        // System.out.println("SUP");
        // for (Point p : supSide) {
        //     System.out.println(p.toString());
        // }
        // System.out.println("----------------------------------");

        Stack<Point> infSide = new Stack<>();
        infSide.push(gallery.get(gallery.size() - 1));
        infSide.push(gallery.get(gallery.size() - 2));

        for (int i = gallery.size() - 3; i >= 0; i--) {
            infSide.push(gallery.get(i));

            while ((infSide.size() > 2) && this.CCW(infSide.get(infSide.size() - 3), infSide.get(infSide.size() - 2), infSide.peek())) {
                infSide.remove(infSide.size() - 2);
            }
        }

        // System.out.println("----------------------------------");
        // System.out.println("INF");
        // for (Point p : infSide) {
        //     System.out.println(p.toString());
        // }
        // System.out.println("----------------------------------");

        supSide.pop();
        infSide.pop();

        ArrayList<Point> convexHull = new ArrayList<>(supSide);
        convexHull.addAll(infSide);
        if (gallery.size() != convexHull.size()) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }

        return convexHull;
    }

    private ArrayList<Point> orderGallery(ArrayList<Point> gallery) {
        gallery.sort(new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                if (p1.getX() == p2.getX()) return Integer.compare((int) p1.getY(), (int) p2.getY());
                return Integer.compare((int) p1.getX(), (int) p2.getX());
            }
        });

        return gallery;
    }

    private ArrayList<ArrayList<Point>> createGalleries(BufferedReader reader) {
        try {
            String line;

            int iterationsLeft = 0;
            int index = 0;
            int galleryLen = Integer.MAX_VALUE;

            ArrayList<ArrayList<Point>> galleries = new ArrayList<>();

            // System.out.println("Iniciando programa!");
            // System.out.println("Tamanho:");
            while ((line = reader.readLine()) != null && !line.equalsIgnoreCase("0")) {
                String[] content = line.split(" ");


                if ((content.length == 1 && iterationsLeft == 0) || (index == galleryLen)) {
                    galleryLen = Integer.parseInt(content[0]);
                    iterationsLeft = galleryLen;
                    index = 0;
                    ArrayList<Point> gallery = new ArrayList<>(Integer.parseInt(content[0]));
                    galleries.add(gallery);
                    continue;
                }
                Point p = new Point(Integer.parseInt(content[0]), Integer.parseInt(content[1]));
                ArrayList<Point> gallery = galleries.get(galleries.size() - 1);
                gallery.add(p);
                iterationsLeft--;
                index++;

                // if (iterationsLeft == 0) {
                //     System.out.println("Tamanho:");
                // }
            }


            return galleries;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean CCW(Point p1, Point p2, Point p3) {
        double value = (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) -
                (p2.getY() - p1.getY()) * (p3.getX() - p1.getX());

        if ( value < 0.000001 ) {
            return true;
        } else {
            return false;
        }
    }
}
