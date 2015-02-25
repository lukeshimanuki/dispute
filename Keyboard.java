import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard extends Controller implements KeyListener
{
	private boolean rightKey;
	private boolean leftKey;
	private boolean jumpKey;
	private boolean a1Key;

	private int rightCode;
	private int leftCode;
	private int jumpCode;
	private int a1Code;

	public Keyboard(int rightCode, int leftCode, int jumpCode, int a1Code)
	{
		this.rightCode = rightCode;
		this.leftCode = leftCode;
		this.jumpCode = jumpCode;
		this.a1Code = a1Code;

		rightKey = false;
		leftKey = false;
		jumpKey = false;
		a1Key = false;
	}

	private void update()
	{
		jump = jumpKey;
		if (rightKey && !leftKey) direction = 1;
		else if (leftKey && !rightKey) direction = -1;
		else direction = 0;
		if (a1Key)
			attack = 1;
		else
			attack = 0;
	}

	@Override
	public void keyTyped(KeyEvent event) {}

	@Override
	public void keyPressed(KeyEvent event)
	{
		int code = event.getKeyCode();
		if (code == rightCode) rightKey = true;
		if (code == leftCode) leftKey = true;
		if (code == jumpCode) jumpKey = true;
		if (code == a1Code) a1Key = true;
		update();
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		int code = event.getKeyCode();
		if (code == rightCode) rightKey = false;
		if (code == leftCode) leftKey = false;
		if (code == jumpCode) jumpKey = false;
		if (code == a1Code) a1Key = false;
		update();
	}
}

