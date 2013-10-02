package example.minehunt.client.flickr;

import javafx.scene.image.Image;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 *
 */
public final class FlickProvider {

    private static final int IMAGE_CACHE_SIZE = 5;

    private static final FlickProvider INSTANCE = new FlickProvider();

    public static FlickProvider getInstance() {
        return INSTANCE;
    }

    private final Queue<Image> imagesQueue;


    private FlickProvider() {
        imagesQueue = new ConcurrentLinkedQueue<>();
    }

    public Image nextImage() {
        //load the next image
        for (int i = imagesQueue.size(); i < IMAGE_CACHE_SIZE; i++) {
            newSingleThreadExecutor().execute(() -> imagesQueue.offer(new FlickrClient().nextImage()));
        }

        if (imagesQueue.isEmpty()) {
            return new Image("mer-surface.jpg");
        } else {
            return imagesQueue.poll();
        }

    }


}
