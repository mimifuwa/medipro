package medipro;

import java.awt.Image;

import medipro.stage.StageModel;
import medipro.tiles.Tile;

public class Entity {

    private final StageModel stageModel;

    private double posX = 0;
    private double posY = 0;
    private double velX = 0;
    private double velY = 0;
    private double accX = 0;
    private double accY = 0;

    private Image image = null;

    private int width = 0;
    private int height = 0;

    private boolean isOnGround = false;
    private boolean isAlive = true;

    public Entity(StageModel stageModel) {
        this.stageModel = stageModel;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;

        if (this.posX + this.velX < 0) {
            this.velX = 0;
        }

        if (this.posX + this.velX + this.width > stageModel.getWorld().getWidth()) {
            this.velX = 0;
        }

        Tile leftTile = getCollisionTileOnLeft(this.posX + this.velX);
        if (leftTile != null) {
            this.velX = 0;
            leftTile.onCollide(this);
        }

        Tile rightTile = getCollisionOnRight(this.posX + this.velX);
        if (rightTile != null) {
            this.velX = 0;
            rightTile.onCollide(this);
        }

        // 絶対値が0.03以下の場合は0にする
        if (Math.abs(this.velX) < 0.03) {
            this.velX = 0;
        }
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;

        if (this.posY + this.velY < 0) {
            this.velY = 0;
            this.posY = 0;
        }

        if (this.posY + this.velY + this.height > stageModel.getWorld().getHeight()) {
            this.velY = 0;
            setAlive(false);
            targetDeathAction();
            resetStageModel();
        }

        Tile topTile = getCollisionOnTop(this.posY + this.velY);
        if (topTile != null) {
            this.velY = 0;
            topTile.onCollide(this);
        }

        Tile bottomTile = getCollisionOnBottom(this.posY + this.velY);
        if (bottomTile != null) {
            this.velY = 0;
            bottomTile.onCollide(this);
            this.isOnGround = true;
        } else {
            this.isOnGround = false;
        }

        // 絶対値が0.03以下の場合は0にする
        if (Math.abs(this.velY) < 0.03) {
            this.velY = 0;
        }
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double accX) {
        this.accX = accX + (-0.025 * this.velX);

        if (this.isOnGround && Math.abs(this.velX) > 0.05) {
            if (this.velX > 0) {
                this.accX -= 0.1;
            } else if (this.velX < 0) {
                this.accX += 0.1;
            }
        }

        // 絶対値が0.03以下の場合は0にする
        if (Math.abs(this.accX) < 0.03) {
            this.accX = 0;
        }
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;

        // 絶対値が0.03以下の場合は0にする
        if (Math.abs(this.accY) < 0.03) {
            this.accY = 0;
        }
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isOnGround() {
        return this.isOnGround;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void resetStageModel() {
        stageModel.reset();
    }

    public void reset() {
        this.posX = stageModel.getWorld().getStartPosX();
        this.posY = stageModel.getWorld().getStartPosY();
        this.velX = 0;
        this.velY = 0;
        this.accX = 0;
        this.accY = 0;
        this.isOnGround = false;
        this.isAlive = true;
    }

    public void targetDeathAction() {
        // TODO: ターゲットが死んだときのアクションを追加する。
    }

    @Override
    public String toString() {
        return "Entity [stageModel=" + stageModel + ", posX=" + posX + ", posY=" + posY + ", velX=" + velX + ", velY="
                + velY + ", accX=" + accX + ", accY=" + accY + ", width=" + width + ", height=" + height + "]";
    }

    public Tile getCollisionTileOnLeft(double newPosX) {
        double[] checkPointsY = {
                this.posY, // 上端
                this.posY + this.height / 3, // 高さの1/3位置
                this.posY + this.height / 3 * 2, // 高さの2/3位置
                this.posY + this.height // 下端
        };
        for (double checkPointY : checkPointsY) {
            Tile tile = stageModel.getWorld().getTileAt(newPosX, checkPointY);
            if (tile != null && tile.isSolid()) {
                return tile;
            }
        }
        return null;
    }

    public Tile getCollisionOnRight(double newPosX) {
        double[] checkPointsY = {
                this.posY, // 上端
                this.posY + this.height / 3, // 高さの1/3位置
                this.posY + this.height / 3 * 2, // 高さの2/3位置
                this.posY + this.height // 下端
        };
        for (double checkPointY : checkPointsY) {
            Tile tile = stageModel.getWorld().getTileAt(newPosX + this.width, checkPointY);
            if (tile != null && tile.isSolid()) {
                return tile;
            }
        }
        return null;
    }

    public Tile getCollisionOnTop(double newPosY) {
        double[] checkPointsX = {
                this.posX, // 左端
                this.posX + this.width / 3, // 幅の1/3位置
                this.posX + this.width / 3 * 2, // 幅の2/3位置
                this.posX + this.width // 右端
        };
        for (double checkPointX : checkPointsX) {
            Tile tile = stageModel.getWorld().getTileAt(checkPointX, newPosY);
            if (tile != null && tile.isSolid()) {
                return tile;
            }
        }
        return null;
    }

    public Tile getCollisionOnBottom(double newPosY) {
        double[] checkPointsX = {
                this.posX, // 左端
                this.posX + this.width / 3, // 幅の1/3位置
                this.posX + this.width / 3 * 2, // 幅の2/3位置
                this.posX + this.width // 右端
        };
        for (double checkPointX : checkPointsX) {
            Tile tile = stageModel.getWorld().getTileAt(checkPointX, newPosY + this.height);
            if (tile != null && tile.isSolid()) {
                return tile;
            }
        }
        return null;
    }

}
