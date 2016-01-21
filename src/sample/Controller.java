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

    @FXML protected TextField PInput;
    @FXML protected TextField KInput;
    @FXML protected TextField CInput;
    @FXML protected TextField qInput;
    @FXML protected TextField alphaInput;
    @FXML protected TextArea Output;

    @FXML
    protected void handleCountButtonAction(ActionEvent event) {
        magic = new StringBuilder();
        P = Double.parseDouble(PInput.getText());
        K = Double.parseDouble(KInput.getText());
        C = Double.parseDouble(CInput.getText());
        q = Double.parseDouble(qInput.getText());
        alpha = Double.parseDouble(alphaInput.getText());
        Count count = new Count(P,K,C,q,alpha);//создаем объект с параметрами
        //count.getLucky();//тут превращаем функцию в оптимиста/пессимиста, если нужно
        double[] minn = {1,0,0,0};//выбираем начальное минимальное значение
        double[] mint = count.solve(minn, 10, 1, 2);
        double minf = count.targetf(minn, mint);
        outer: for (int i = 1; i <= 1; i++) {//циклы для перебора целых чисел, тут можно поменять макс. количество контейнеров
            for (int j = 0; j <= i; j++) {
                for (int k = 0; k <= j; k++) {
                    for (int l = 0; l <= k; l++) {
                        double[] n = {i,j,k,l};
                        double[] t = count.solve(n, 10, 1, 2);
                        if(t != null){//если ответ существует, то проверяем минимальность
                            double f = count.targetf(n, t);
                            if (f < minf){
                                minf = f;
                                minn = n;
                                mint = t;
                            }
                        } else continue outer;
                    }
                }
            }
        }

        magic.append("Четкая задача\n");
        magic.append("Количество контейнеров: " + (int)minn[0] + "\n");//выводим на экран
        magic.append("Размеры: " + Arrays.toString(mint) + "\n");
        magic.append("Стоимость: " + minf + "\n");


        count = new Count(P,K,C,q,alpha);//создаем объект с параметрами
        count.getLucky();//тут превращаем функцию в оптимиста/пессимиста, если нужно
        minn = new double[]{1,0,0,0};//выбираем начальное минимальное значение
        mint = count.solve(minn, 10, 1, 2);
        minf = count.targetf(minn, mint);
        outer: for (int i = 1; i <= 1; i++) {//циклы для перебора целых чисел, тут можно поменять макс. количество контейнеров
            for (int j = 0; j <= i; j++) {
                for (int k = 0; k <= j; k++) {
                    for (int l = 0; l <= k; l++) {
                        double[] n = {i,j,k,l};
                        double[] t = count.solve(n, 10, 1, 2);
                        if(t != null){//если ответ существует, то проверяем минимальность
                            double f = count.targetf(n, t);
                            if (f < minf){
                                minf = f;
                                minn = n;
                                mint = t;
                            }
                        } else continue outer;
                    }
                }
            }
        }

        magic.append("Задача оптимиста:\n");
        magic.append("Количество контейнеров: " + (int)minn[0] + "\n");//выводим на экран
        magic.append("Размеры: " + Arrays.toString(mint) + "\n");
        magic.append("Стоимость: " + minf + "\n");


        count = new Count(P,K,C,q,alpha);//создаем объект с параметрами
        count.getUnLucky();//тут превращаем функцию в оптимиста/пессимиста, если нужно
        minn = new double[]{1,0,0,0};//выбираем начальное минимальное значение
        mint = count.solve(minn, 10, 1, 2);
        minf = count.targetf(minn, mint);
        outer: for (int i = 1; i <= 1; i++) {//циклы для перебора целых чисел, тут можно поменять макс. количество контейнеров
            for (int j = 0; j <= i; j++) {
                for (int k = 0; k <= j; k++) {
                    for (int l = 0; l <= k; l++) {
                        double[] n = {i,j,k,l};
                        double[] t = count.solve(n, 10, 1, 2);
                        if(t != null){//если ответ существует, то проверяем минимальность
                            double f = count.targetf(n, t);
                            if (f < minf){
                                minf = f;
                                minn = n;
                                mint = t;
                            }
                        } else continue outer;
                    }
                }
            }
        }

        magic.append("Задача пессимиста:\n");
        magic.append("Количество контейнеров: " + (int)minn[0] + "\n");//выводим на экран
        magic.append("Размеры: " + Arrays.toString(mint) + "\n");
        magic.append("Стоимость: " + minf + "\n");

        Output.setText(magic.toString());
    }
}
