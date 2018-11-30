import processing.core.PImage;

import java.util.*;

final class WorldModel {
    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill( this.background[row], defaultBackground );
        }
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows() {
        return numRows;
    }

    public Set<Entity> entities() {
        return entities;
    }


    public void addEntity(Entity entity) {
        if (withinBounds( entity.getPosition() )) {
            setOccupancyCell( entity.getPosition(), entity );
            this.entities.add( entity );
        }
    }

    public Entity[][] getOccupancy() {
        return occupancy;
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds( pos ) && !pos.equals( oldPos )) {
            setOccupancyCell( oldPos, null );
            removeEntityAt( pos );
            setOccupancyCell( pos, entity );
            entity.setPosition( pos );
        }
    }

    public void removeEntity(Entity entity) {
        removeEntityAt( entity.getPosition() );
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds( pos )
                && getOccupancyCell( pos ) != null) {
            Entity entity = getOccupancyCell( pos );

         /* this moves the entity just outside of the grid for
            debugging purposes */
            entity.setPosition( new Point( -1, -1 ) );
            this.entities.remove( entity );
            setOccupancyCell( pos, null );
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos) {
        if (withinBounds( pos )) {
            return Optional.of( getBackgroundCell( pos ).getCurrentImage() );
        } else {
            return Optional.empty();
        }
    }

    public void setBackground(Point pos, Background background) {
        if (withinBounds( pos )) {
            setBackgroundCell( pos, background );
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied( pos )) {
            return Optional.of( getOccupancyCell( pos ) );
        } else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    public void setOccupancyCell(Point pos, Entity entity) {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }

    public void setBackgroundCell(Point pos, Background background) {
        this.background[pos.y][pos.x] = background;
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < numRows &&
                pos.x >= 0 && pos.x < numCols;
    }

    public boolean isOccupied(Point pos) {
        return withinBounds( pos ) &&
                getOccupancyCell( pos ) != null;
    }

    public void tryAddEntity(Entity entity) {

        if (isOccupied( entity.getPosition() )) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException( "position occupied" );
        }

        addEntity( entity );
    }

    public Optional<Entity> findNearest(Point pos, Entity kind) {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.entities()) {
            if (entity.getClass().getName() == kind.getClass().getName()) {
                ofType.add( entity );
            }
        }

        return pos.nearestEntity( ofType );
    }

}


