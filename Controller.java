public abstract class Controller
{
	public int attack; // 0: nothing 1: body 2: right 3: left 4: up 5: recover
	public int direction; // 0: neutral 1: right -1: left
	public boolean jump;

	public void update() {}
}

