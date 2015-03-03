import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard extends Controller implements KeyListener
{
	private boolean rightKey;
	private boolean leftKey;
	private boolean jumpKey;
	private boolean a1Key;
	private boolean a2Key;
	private boolean a3Key;
	private boolean a4Key;
	private boolean a5Key;

	private int rightCode;
	private int leftCode;
	private int jumpCode;
	private int a1Code;
	private int a2Code;
	private int a3Code;
	private int a4Code;
	private int a5Code;

	public Keyboard(int rightCode, int leftCode, int jumpCode, int a1Code, int a2Code, int a3Code, int a4Code, int a5Code)
	{
		this.rightCode = rightCode;
		this.leftCode = leftCode;
		this.jumpCode = jumpCode;
		this.a1Code = a1Code;
		this.a2Code = a2Code;
		this.a3Code = a3Code;
		this.a4Code = a4Code;
		this.a5Code = a5Code;

		rightKey = false;
		leftKey = false;
		jumpKey = false;
		a1Key = false;
		a2Key = false;
		a3Key = false;
		a4Key = false;
		a5Key = false;
	}

	@Override
	public void update()
	{
		jump = jumpKey;

		if (rightKey && !leftKey) direction = 1;
		else if (leftKey && !rightKey) direction = -1;
		else direction = 0;
	
		if (a1Key) attack = 1;
		else if (a2Key) attack = 2;
		else if (a3Key) attack = 3;
		else if (a4Key) attack = 4;
		else if (a5Key) attack = 5;
		else attack = 0;
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
		if (code == a2Code) a2Key = true;
		if (code == a3Code) a3Key = true;
		if (code == a4Code) a4Key = true;
		if (code == a5Code) a5Key = true;
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		int code = event.getKeyCode();
		if (code == rightCode) rightKey = false;
		if (code == leftCode) leftKey = false;
		if (code == jumpCode) jumpKey = false;
		if (code == a1Code) a1Key = false;
		if (code == a2Code) a2Key = false;
		if (code == a3Code) a3Key = false;
		if (code == a4Code) a4Key = false;
		if (code == a5Code) a5Key = false;
	}
}

