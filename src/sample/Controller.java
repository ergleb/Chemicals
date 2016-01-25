package sample;

import count.Count;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Arrays;

public class Controller {
    private StringBuilder magic;
    double P;
    double C;
    double K;
    double q;
    double alpha;
    double[] tMax;

    @FXML protected TextField PInput;
    @FXML protected TextField KInput;
    @FXML protected TextField CInput;
    @FXML protected TextField qInput;
    @FXML protected TextField alphaInput;
    @FXML protected TextField t1Input;
    @FXML protected TextField t2Input;
    @FXML protected TextField t3Input;
    @FXML protected TextArea Output;

    @FXML
    protected void handleCountButtonAction(ActionEvent event) {
        magic = new StringBuilder();
        P = Double.parseDouble(PInput.getText());
        K = Double.parseDouble(KInput.getText());
        C = Double.parseDouble(CInput.getText());
        q = Double.parseDouble(qInput.getText());
        alpha = Double.parseDouble(alphaInput.getText());
        tMax = new double[3];
        tMax[0] = Double.parseDouble(t1Input.getText());
        tMax[1] = Double.parseDouble(t2Input.getText());
        tMax[2] = Double.parseDouble(t3Input.getText());
        Count count = new Count(P,K,C,q,alpha,tMax);//создаем объект с параметрами
        //count.getLucky();//тут превращаем функцию в оптимиста/пессимиста, если нужно
        double mincont = Math.ceil(count.getP()/(6*4*3));
        double minn = 0;
        double minn0 = 0;
        double[] mint = null;
        double minf = Double.MAX_VALUE;
        outer: for (int i = (int)mincont; i <= (int)mincont+2; i++) {//циклы для перебора целых чисел, тут можно поменять макс. количество контейнеров
            for (int j = 0; j <= i; j++) {
                double[] t = count.solve(i,j,10,1,2);
                if (t!=null) {
                    if(count.targetf(i,j,t)<minf){
                        minn = i;
                        minn0 = j;
                        mint = t;
                        minf = count.targetf(i,j,t);
                    }
                }
                else continue outer;
            }
        }
        magic.append("Четкая задача\n");
        magic.append("Количество контейнеров: " + minn + "\n");//выводим на экран
        magic.append("Размеры: " + Arrays.toString(mint) + "\n");
        magic.append("Стоимость: " + minf + "\n");


        count = new Count(P,K,C,q,alpha,tMax);//создаем объект с параметрами
        count.getLucky();//тут превращаем функцию в оптимиста/пессимиста, если нужно
        mincont = Math.ceil(count.getP()/(6*4*3));
        minn = 0;
        minn0 = 0;
        mint = null;
        minf = Double.MAX_VALUE;
        outer: for (int i = (int)mincont; i <= (int)mincont+2; i++) {//циклы для перебора целых чисел, тут можно поменять макс. количество контейнеров
            for (int j = 0; j <= i; j++) {
                double[] t = count.solve(i,j,10,1,2);
                System.out.println(i + " " + j);
                if (t!=null) {
                    System.out.println(Arrays.toString(t));
                    System.out.println(count.targetf(i,j,t));
                    if(count.targetf(i,j,t)<minf){
                        minn = i;
                        minn0 = j;
                        mint = t;
                        minf = count.targetf(i,j,t);
                    }
                }
                else continue outer;
            }
        }

        magic.append("Задача оптимиста:\n");
        magic.append("Количество контейнеров: " + minn+ "\n");//выводим на экран
        magic.append("Размеры: " + Arrays.toString(mint) + "\n");
        magic.append("Стоимость: " + minf + "\n");


        count = new Count(P,K,C,q,alpha,tMax);//создаем объект с параметрами
        count.getUnLucky();//тут превращаем функцию в оптимиста/пессимиста, если нужно
        mincont = Math.ceil(count.getP()/(6*4*3));
        minn = 0;
        minn0 = 0;
        mint = null;
        minf = Double.MAX_VALUE;
        outer: for (int i = (int)mincont; i <= (int)mincont+2; i++) {//циклы для перебора целых чисел, тут можно поменять макс. количество контейнеров
            for (int j = 0; j <= i; j++) {
                double[] t = count.solve(i,j,10,1,2);
                if (t!=null) {
                    if(count.targetf(i,j,t)<minf){
                        minn = i;
                        minn0 = j;
                        mint = t;
                        minf = count.targetf(i,j,t);
                    }
                }
                else continue outer;
            }
        }

        magic.append("Задача пессимиста:\n");
        magic.append("Количество контейнеров: " + minn+ "\n");//выводим на экран
        magic.append("Размеры: " + Arrays.toString(mint) + "\n");
        magic.append("Стоимость: " + minf + "\n");

        Output.setText(magic.toString());
    }
}
