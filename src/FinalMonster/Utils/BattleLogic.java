package FinalMonster.Utils;

import FinalMonster.Parser.Move;
import FinalMonster.Parser.Pokemon;

import java.util.Random;

public class BattleLogic {
	private static Random rand = new Random();

	public static AttackResult attack(Pokemon defendingPokemon, int attackingPower, Move attackingMove) {
		int missChance = 100 - attackingMove.getAccuracy();
		boolean miss;

		if ( missChance >= 1 )
			miss = rand.nextInt(missChance) == 1;
		else
			miss = false;

		if ( miss )
			return new AttackResult(AttackStatus.MISS, defendingPokemon.getHp());

		double multiplier = getMultiplierPower(attackingMove.getType(), defendingPokemon.getType());
		int damage = (int) ( ( ( ( attackingPower * attackingMove.getPower() / defendingPokemon.getDefense() )
				/ 20 )
				+ 2 )
				* multiplier );

		int newHP = defendingPokemon.getHp() - damage;
		newHP = ( newHP < 0 ) ? 0 : newHP;

		return new AttackResult(multiplier, newHP);
	}

	private static double getMultiplierPower(String attType, String defType) {
		//todo: real multiplyer calculation from https://pokemondb.net/type
		return 1;
	}

	public static class AttackResult {
		private AttackStatus status;
		private int newHP;

		private AttackResult(AttackStatus status, int newHP) {
			this.status = status;
			this.newHP = newHP;
		}

		private AttackResult(double multiplier, int newHP) {
			if ( multiplier == 0 )
				this.status = AttackStatus.NOT_EFFECTIVE;
			else if ( multiplier == 1 )
				this.status = AttackStatus.HIT;

			else if ( multiplier == 2 )
				this.status = AttackStatus.SUPER_EFFECTIVE;
			else if ( multiplier == 0.5 ) {
				this.status = AttackStatus.NOT_VERY_EFFECTIVE;
			}
			this.newHP = newHP;
		}

		public AttackStatus getStatus() {
			return status;
		}

		public int getNewHP() {
			return newHP;
		}
	}

	public enum AttackStatus {
		MISS,
		HIT,
		SUPER_EFFECTIVE,
		NOT_VERY_EFFECTIVE,
		NOT_EFFECTIVE
	}

	public static Move getOpponentMove(Pokemon pokemon) {
		Move[] moves = pokemon.getMoves();
		assert moves.length == 4;

		int index = rand.nextInt(4);
		return moves[index];
	}
}
