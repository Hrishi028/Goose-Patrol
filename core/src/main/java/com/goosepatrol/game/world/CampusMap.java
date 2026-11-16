package com.goosepatrol.game.world;

public class CampusMap {
    private final float width;
    private final float height;

    public CampusMap(float width, float height){
        this.width = width;
        this.height = height;

    }

    public boolean isBlocked(float x , float y , float radius){
        if (insideRectangle(x,y,radius,width * 0.025f,height * 0.69f,width *0.25f, height * 0.22f)) return true;
        if (insideRectangle(x,y,radius,width *0.735f,height * 0.70f,width * 0.24f, height * 0.22f)) return true;
        if (insideRectangle(x,y,radius,width * 0.025f,height * 0.055f,width * 0.25f,height * 0.22f)) return true;
        if (insideRectangle(x,y,radius,width * 0.725f,height * 0.055f,width * 0.25f,height * 0.22f)) return true;
        
        float lakeWidth = width * 0.23f;
        float lakeHeight = height * 0.17f;
        
        float lakeCenterX = width * 0.60f + lakeWidth / 2f;
        float lakeCenterY = height * 0.58f + lakeHeight / 2f;
        float dx = (x - lakeCenterX) / (lakeWidth / 2f + radius);
        float dy = (y - lakeCenterY) / (lakeHeight / 2f + radius);
        
        return dx * dx + dy *dy < 1f;

    }
    public boolean isBlockedOrOutside(float x , float y , float radius){
        if (x - radius < 0 || y - radius < 0 || x + radius > width || y + radius > height){
            return true;
        }
        return isBlocked(x, y, radius);
    }
    
    public boolean insideRectangle(float x , float y ,float r, float rx,float ry ,float rw ,float rh){
        return x + r > rx && x - r < rx + rw && y + r > ry && y - r < ry + rh;
    }

    public float getWidth() {return width; }
    public float getHeight() {return height; }

}
