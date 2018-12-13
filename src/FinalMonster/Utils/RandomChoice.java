package FinalMonster.Utils;

import FinalMonster.Parser.Pokemon;

import java.util.Arrays;
import java.util.Random;

public class RandomChoice {

	public static Pokemon[] randomPokemons(Pokemon[] src, int length) {
		assert length <= src.length;
		Random r = new Random();

		Pokemon[] result = new Pokemon[length];
		for ( int i = 0; i < length; i++ ) {
			Pokemon tmp = src[r.nextInt(length)];
			if ( Arrays.binarySearch(src, tmp) == -1 ) {
				result[i] = tmp;
			} else {
				i--;
			}
		}
		return result;
	}
}
