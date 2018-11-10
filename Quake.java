import processing.core.PImage;

import java.util.List;


public class Quake extends AnimatedEntity {
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;


    public Quake(Point position, List<PImage> images) {

        super( QUAKE_ID, position, images, 0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD );
    }

    //needed for ActiveEntity
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents( this );
        world.removeEntity( this );
    }

    @Override //AnimatedEntity
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), getActionPeriod() );
        scheduler.scheduleEvent( this, new Animation( this, QUAKE_ANIMATION_REPEAT_COUNT ), animationPeriod );
    }
}