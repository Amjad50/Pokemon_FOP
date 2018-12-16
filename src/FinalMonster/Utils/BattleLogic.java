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
		double mult = 1;
		switch ( attType ) {
			case "Normal":
				switch ( defType ) {
					case "Rock":
					case "Steel":
						mult = 0.5;
						break;
					case "Ghost":
						mult = 0;
						break;
					default:
						mult = 1;
				}
				break;
			case "Fire":
				switch ( defType ) {
					case "Fire":
					case "Water":
					case "Rock":
					case "Dragon":
						mult = 0.5;
						break;
					case "Grass":
					case "Ice":
					case "Bug":
					case "Steel":
						mult = 2;
						break;
					default:
						mult = 1;

				}
				break;
			case "Water":
				switch ( defType ) {
					case "Water":
					case "Grass":
					case "Dragon":
						mult = 0.5;
						break;
					case "Fire":
					case "Ground":
					case "Rock":
						mult = 2;
						break;
					default:
						mult = 1;

				}
				break;
			case "Electric":
				switch ( defType ) {
					case "Electric":
					case "Grass":
					case "Dragon":
						mult = 0.5;
						break;
					case "Water":
					case "Flying":
						mult = 2;
						break;
					case "Ground":
						mult = 0;
						break;
					default:
						mult = 1;
						break;
				}
				break;
			case "Grass":
				switch ( defType ) {
					case "Fire":
					case "Grass":
					case "Poison":
					case "Flying":
					case "Bug":
					case "Steel":
					case "Dragon":
						mult = 0.5;
						break;
					case "Water":
					case "Rock":
					case "Ground":
						mult = 2;
						break;
					default:
						mult = 1;
						break;
				}
				break;
			case "Ice":
				switch ( defType ) {
					case "Fire":
					case "Water":
					case "Ice":
					case "Steel":
						mult = 0.5;
						break;
					case "Grass":
					case "Dragon":
					case "Ground":
					case "Flying":
						mult = 2;
						break;
					default:
						mult = 1;
				}
				break;
			case "Fighting":
				switch ( defType ) {
					case "Flying":
					case "Psychic":
					case "Bug":
					case "Fairy":
					case "Posion":
						mult = 0.5;
						break;
					case "Normal":
					case "Ice":
					case "Steel":
					case "Rock":
					case "Dark":
						mult = 2;
						break;
					case "Ghost":
						mult = 0;
						break;
					default:
						mult = 1;
				}
				break;
			case "Poison":
				switch ( defType ) {
					case "Poison":
					case "Ground":
					case "Rock":
					case "Ghost":
						mult = 0.5;
						break;
					case "Grass":
					case "Fairy":
						mult = 2;
						break;
					case "Steel":
						mult = 0;
						break;
					default:
						mult = 1;
				}
				break;
			case "Ground":
				switch ( defType ) {
					case "Grass":
					case "Bug":
						mult = 0.5;
						break;
					case "Fire":
					case "Steel":
					case "Electric":
					case "Poison":
					case "Rock":
						mult = 2;
						break;
					case "Flying":
						mult = 0;
						break;
					default:
						mult = 1;
				}
				break;
			case "Flying":
				switch ( defType ) {
					case "Electric":
					case "Steel":
					case "Rock":
						mult = 0.5;
						break;
					case "Grass":
					case "Fighting":
					case "Bug":
						mult = 2;
						break;
					default:
						mult = 1;
				}
				break;
			case "Psychic":
				switch ( defType ) {
					case "Psychic":
					case "Steel":
						mult = 0.5;
						break;
					case "Fighting":
					case "Poison":
						mult = 2;
						break;
					case "Dark":
						mult = 0;
						break;
					default:
						mult = 1;
				}
				break;
			case "Bug":
				switch ( defType ) {
					case "Fire":
					case "Fighting":
					case "Flying":
					case "Poison":
					case "Ghost":
					case "Steel":
					case "Fairy":
						mult = 0.5;
						break;
					case "Grass":
					case "Dark":
					case "Psychic":
						mult = 2;
						break;
					default:
						mult = 1;
				}
				break;
			case "Rock":
				switch ( defType ) {
					case "Fighting":
					case "Ground":
					case "Steel":
						mult = 0.5;
						break;
					case "Fire":
					case "Ice":
					case "Bug":
					case "Flying":
						mult = 2;
						break;
					default:
						mult = 1;
				}
				break;
			case "Ghost":
				switch ( defType ) {
					case "Dark":
						mult = 0.5;
						break;
					case "Psychic":
					case "Ghost":
						mult = 2;
						break;
					case "Normal":
						mult = 0;
						break;
					default:
						mult = 1;
				}
				break;
			case "Dragon":
				switch ( defType ) {
					case "Steel":
						mult = 0.5;
						break;
					case "Dragon":
						mult = 2;
						break;
					case "Fairy":
						mult = 0;
						break;
					default:
						mult = 1;
				}
				break;
			case "Dark":
				switch ( defType ) {
					case "Fighting":
					case "Dark":
					case "Fairy":
						mult = 0.5;
						break;
					case "Psychic":
					case "Ghost":
						mult = 2;
						break;
					default:
						mult = 1;
				}
				break;
			case "Steel":
				switch ( defType ) {
					case "Fire":
					case "Water":
					case "Electric":
					case "Steel":
						mult = 0.5;
						break;
					case "Rock":
					case "Ice":
					case "Fairy":
						mult = 2;
						break;
					default:
						mult = 1;
				}
				break;
			case "Fairy":
				switch ( defType ) {
					case "Fire":
					case "Posion":
					case "Steel":
						mult = 0.5;
						break;
					case "Dragon":
					case "Fighting":
					case "Dark":
						mult = 2;
						break;
					default:
						mult = 1;
				}


		}
		return mult;

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
