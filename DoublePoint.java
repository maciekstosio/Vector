class DoublePoint{
    private double x;
    private double y;

    DoublePoint(){
        x=0;
        y=0;
    }

    DoublePoint(double x, double y){
        this.x=x;
        this.y=y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setLocation(double x, double y){
        this.x=x;
        this.y=y;
    }

    public void translate(double dx, double dy){
        x+=dx;
        y+=dy;
    }
}
