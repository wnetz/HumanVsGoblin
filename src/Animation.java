import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Animation {

    private boolean stopped;                // has animations stopped
    private int animationDirection;         // animation direction (i.e counting forward or backward)
    private int currentFrame;               // animations current frame
    private int frameCount;                 // Counts ticks for change
    private int totalFrames;                // total amount of frames for your animation
    private List<Frame> frames = new ArrayList<>();    // Arraylist of frames

    public Animation(Frame[] f)
    {
        this.stopped = true;
        this.animationDirection = 1;
        this.currentFrame = 0;
        this.frameCount = 0;

        for (Frame frame: f)
        {
            addFrame(frame);
        }
        this.totalFrames = this.frames.size()-1;
    }

    public void start()
    {
        if (!stopped)
            return;

        if (frames.size() == 0)
            return;

        stopped = false;
    }
    public void restart()
    {
        if (frames.size() == 0)
            return;

        stopped = false;
        currentFrame = 0;
    }

    public void stop()
    {
        if (frames.size() == 0)
            return;

        stopped = true;
    }
    public void reset()
    {
        this.stopped = true;
        this.frameCount = 0;
        this.currentFrame = 0;
    }

    public void update()
    {
        if (!stopped) {
            frameCount++;

            if (frameCount > frames.get(currentFrame).getDuration()) {
                frameCount = 0;
                currentFrame += animationDirection;
                if (currentFrame > totalFrames - 1) {
                    currentFrame = 0;
                }
                else if (currentFrame < 0) {
                    currentFrame = totalFrames - 1;
                }
            }
        }

    }

    private void addFrame(Frame frame)
    {

        frames.add(frame);
        currentFrame = 0;
    }

    public BufferedImage getSprite()
    {
        return frames.get(currentFrame).getFrame();
    }



}