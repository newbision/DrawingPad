package com.newbision.drawingpad.utils;

public class AppCalculateZoomLevel {
    public static int getZoomPercentage(float scaleFactor){
        int zoomPercentage = 0;

        if(scaleFactor <= 0.03){
            zoomPercentage = 10;
        }
        else if(scaleFactor > 0.03 && scaleFactor <= 0.08){
            zoomPercentage = 15;
        }
        else if(scaleFactor > 0.08 && scaleFactor <= 0.12){
            zoomPercentage = 20;
        }
        else if(scaleFactor > 0.12 && scaleFactor <= 0.16){
            zoomPercentage = 25;
        }
        else if(scaleFactor > 0.16 && scaleFactor <= 0.20){
            zoomPercentage = 30;
        }
        else if(scaleFactor > 0.20 && scaleFactor <= 0.24){
            zoomPercentage = 35;
        }
        else if(scaleFactor > 0.24 && scaleFactor <= 0.28){
            zoomPercentage = 40;
        }
        else if(scaleFactor > 0.28 && scaleFactor <= 0.32){
            zoomPercentage = 45;
        }
        else if(scaleFactor > 0.32 && scaleFactor <= 0.36){
            zoomPercentage = 50;
        }
        else if(scaleFactor > 0.36 && scaleFactor <= 0.40){
            zoomPercentage = 55;
        }
        else if(scaleFactor > 0.40 && scaleFactor <= 0.42){
            zoomPercentage = 60;
        }
        else if(scaleFactor > 0.42 && scaleFactor <= 0.46){
            zoomPercentage = 65;
        }
        else if(scaleFactor > 0.46 && scaleFactor <= 0.50){
            zoomPercentage = 70;
        }
        else if(scaleFactor > 0.50 && scaleFactor <= 0.60){
            zoomPercentage = 75;
        }
        else if(scaleFactor > 0.60 && scaleFactor <= 0.70){
            zoomPercentage = 80;
        }
        else if(scaleFactor > 0.70 && scaleFactor <= 0.80){
            zoomPercentage = 85;
        }
        else if(scaleFactor > 0.80 && scaleFactor <= 0.90){
            zoomPercentage = 90;
        }
        else if(scaleFactor > 0.90 && scaleFactor <= 0.99){
            zoomPercentage = 95;
        }
        else if(scaleFactor == 1.0){
            zoomPercentage = 100;
        }
        else if(scaleFactor > 1.0 && scaleFactor <= 1.05){
            zoomPercentage = 110;
        }
        else if(scaleFactor > 1.05 && scaleFactor <= 1.1){
            zoomPercentage = 120;
        }
        else if(scaleFactor > 1.1 && scaleFactor <= 1.15){
            zoomPercentage = 130;
        }
        else if(scaleFactor > 1.15 && scaleFactor <= 1.2){
            zoomPercentage = 140;
        }
        else if(scaleFactor > 1.2 && scaleFactor <= 1.25){
            zoomPercentage = 150;
        }
        else if(scaleFactor > 1.25 && scaleFactor <= 1.3){
            zoomPercentage = 160;
        }
        else if(scaleFactor > 1.3 && scaleFactor <= 1.35){
            zoomPercentage = 170;
        }
        else if(scaleFactor > 1.35 && scaleFactor <= 1.45){
            zoomPercentage = 180;
        }
        else if(scaleFactor > 1.45 && scaleFactor <= 1.47){
            zoomPercentage = 190;
        }
        else if(scaleFactor >= 1.47 && scaleFactor <= 1.55){
            zoomPercentage = 200;
        }
        else if(scaleFactor > 1.55 && scaleFactor <= 1.6){
            zoomPercentage = 210;
        }
        else if(scaleFactor > 1.6 && scaleFactor <= 1.65){
            zoomPercentage = 220;
        }
        else if(scaleFactor > 1.65 && scaleFactor <= 1.7){
            zoomPercentage = 230;
        }
        else if(scaleFactor > 1.7 && scaleFactor <= 1.75){
            zoomPercentage = 240;
        }
        else if(scaleFactor > 1.75 && scaleFactor <= 1.8){
            zoomPercentage = 250;
        }
        else if(scaleFactor > 1.8 && scaleFactor <= 1.9){
            zoomPercentage = 260;
        }
        else if(scaleFactor > 1.9 && scaleFactor <= 2.0){
            zoomPercentage = 270;
        }
        else if(scaleFactor > 2.0 && scaleFactor <= 2.05){
            zoomPercentage = 280;
        }
        else if(scaleFactor > 2.05 && scaleFactor <= 2.1){
            zoomPercentage = 290;
        }
        else if(scaleFactor > 2.1){
            zoomPercentage = 300;
        }

        return zoomPercentage;
    }
}
