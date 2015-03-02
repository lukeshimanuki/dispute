import java.awt.Image;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class Sprite
{
	private Image image;

	private int width;
	private int height;

	private int centerX;
	private int centerY;

	public Sprite(String filename, int width, int height, int centerX, int centerY)
	{
		this.width = width;
		this.height = height;
		this.centerX = centerX;
		this.centerY = centerY;
		try
		{
			image = ImageIO.read(new File(filename)).getScaledInstance(width, height, Image.SCALE_DEFAULT);
		}
		catch (IOException exception)
		{
			// error
		}
	}

	public void draw(Graphics2D graphics, int x, int y)
	{
		graphics.drawImage(image, x - centerX, y - centerY, null);
	}
}
