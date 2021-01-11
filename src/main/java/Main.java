import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class Main
{
    public static void main(String[] args)
    {
        String imgFolder = "data/images";
        String link = "https://lenta.ru";
        getImagesByLink(link, imgFolder);
    }

    private static void getImagesByLink(String link, String imgFolder)
    {
        Document document = null;
        try {
            document = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i = 0;
        assert document != null;
        for (Element img : document.select("img[src~=(?i)\\.(png|jpe?g|gif)]")) {
            String absSrc = img.absUrl("src");
            System.out.println(i++ + ") " + getImageName(absSrc));
            saveImg(absSrc, imgFolder);
        }
    }

    private static String getImageName(String srcUrl)
    {
        return srcUrl.substring(srcUrl.lastIndexOf("/") + 1);
    }

    private static String getFormatName(String srcUrl)
    {
        return getImageName(srcUrl).substring(getImageName(srcUrl).lastIndexOf(".") + 1);
    }

    private static void saveImg(String imgSrc, String imgFolder)
    {
        try {
            URL imgUrl = new URL(imgSrc);
            BufferedImage img = ImageIO.read(imgUrl);
            ImageIO.write(img, getFormatName(imgSrc),
                          new File(imgFolder + File.separator + getImageName(imgSrc)));
        } catch (IOException ex)
        {
            System.out.println("Can`t save image cause:");
            ex.printStackTrace();
        }
    }

    public ImageOutputStream createOutputStreamInstance(Object output,
                                                        boolean useCache,
                                                        File cacheDir) {
        if (output instanceof File) {
            try {
                return new FileImageOutputStream((File)output);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
