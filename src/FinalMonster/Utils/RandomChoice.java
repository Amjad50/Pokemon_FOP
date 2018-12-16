package FinalMonster.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomChoice {

	public static <T> ArrayList<T> random(List<T> src, int length) {
		assert length <= src.size();
		Random r = new Random();
		boolean reverse;
		int lengthS;
		if ( src.size() / 2 < length ) {
			reverse = true;
			lengthS = src.size() - length;
		} else {
			reverse = false;
			lengthS = length;
		}
		int[] indecis = new int[lengthS];
		ArrayList<T> result = new ArrayList<>(length);

		for ( int i = 0; i < lengthS; i++ ) {
			indecis[i] = r.nextInt(src.size());
			for ( int j = 0; j < i; j++ ) {
				if ( indecis[j] == indecis[i] ) {
					i--;
					break;
				}
			}
		}
		if ( reverse ) {
			List indciesl = Arrays.asList(indecis);
			for ( int i = 0; i < src.size(); i++ ) {
				if ( !indciesl.contains(i) )
					result.add(src.get(i));
			}
		} else {
			for ( int i = 0; i < length; i++ ) {
				result.add(src.get(indecis[i]));
			}
		}
		return result;
	}
}
