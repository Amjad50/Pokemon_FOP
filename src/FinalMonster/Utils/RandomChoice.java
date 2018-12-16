package FinalMonster.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomChoice {

	public static <T> ArrayList<T> random(List<T> src, int length) {
		assert length <= src.size();
		Random r = new Random();
		int[] indecis = new int[length];
		ArrayList<T> result = new ArrayList<>(length);

		for ( int i = 0; i < length; i++ ) {
			indecis[i] = r.nextInt(src.size());
			for ( int j = 0; j < i; j++ ) {
				if ( indecis[j] == indecis[i] ) {
					i--;
					break;
				}
			}
		}
		for ( int i = 0; i < length; i++ ) {
			result.add(src.get(indecis[i]));
		}
		return result;
	}
}
