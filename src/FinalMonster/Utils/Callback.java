package FinalMonster.Utils;

public interface Callback {

	void call();

	interface WithArg<V> {
		void call(V arg);
	}
}