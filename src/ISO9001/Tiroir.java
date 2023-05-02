package ISO9001;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Tiroir {
    Pane pane;
    Text texta;
    Text textb;
    public Tiroir(String num) {
        this.pane = new Pane();
        Rectangle rectangle = new Rectangle();
        rectangle.setX(0);
        rectangle.setY(0);
        rectangle.setWidth(150);
        rectangle.setHeight(300);
        rectangle.setFill(Paint.valueOf("black"));
        Rectangle rectangle2 = new Rectangle();
        rectangle2.setX(5);
        rectangle2.setY(5);
        rectangle2.setWidth(140);
        rectangle2.setHeight(290);
        rectangle2.setFill(Paint.valueOf("white"));
        Line line = new Line();
        line.setStartX(75);
        line.setEndX(75);
        line.setStartY(3);
        line.setEndY(297);
        line.setFill(Paint.valueOf("black"));
        line.getStrokeDashArray().addAll(25d, 10d);
        this.texta = new Text(20, 320, "T" + num + "a");
        this.texta.setFont(new Font(20));
        this.textb = new Text(95,320, "T" + num + "b");
        this.textb.setFont(new Font(20));
        pane.getChildren().addAll(rectangle, rectangle2, line, this.texta, this.textb);
        rectangle2.setOnMouseClicked(e ->  {
            if (e.getX() < line.getEndX()) {
                StockagePhysique.setTable(num + "a");
                StockagePhysique.clearRed();
                this.texta.setFill(Color.RED);
            } else {
                StockagePhysique.setTable(num + "b");
                StockagePhysique.clearRed();
                this.textb.setFill(Color.RED);
            }
        });
    }

    public void clear() {
        this.texta.setFill(Color.BLACK);
        this.textb.setFill(Color.BLACK);
    }

    public void setRed(String letter) {
        if (letter.equals("a")) {
            this.texta.setFill(Color.RED);
        } else {
            this.textb.setFill(Color.RED);
        }
    }

}
