package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Constrains;
import FinalMonster.Graphics.Storage.ImageDB;
import FinalMonster.Graphics.Storage.Music;
import FinalMonster.Graphics.Utils;
import FinalMonster.Parser.Move;
import FinalMonster.Parser.Pokemon;
import FinalMonster.Player;
import FinalMonster.Utils.BattleLogic;
import FinalMonster.Utils.Callback;
import FinalMonster.Utils.SwitchPokemon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static java.util.stream.Collectors.toList;

public class BattleScene extends StackPane {

	public enum Who {
		PLAYER,
		OPPONENT
	}

	public static final int OPPONENT_X_BOTTOM = 500,
			OPPONENT_Y_BOTTOM = 350,
			PLAYER_X_CENTER = 250,
			PLAYER_Y_CENTER = 400;

	public static final int EXP_ON_WIN = 10;

	private final double MARGIN_SIZE = 20,
			STATUS_WIDTH = 300,
			STATUS_HEIGHT = 50,
			BOTTOM_HEIGHT = 100;
	@FXML
	private Pane status_pane;
	@FXML
	private Pane normal_pane;

	@FXML
	private Pane root;
	@FXML
	private StatusBar status_opponent;
	@FXML
	private StatusBar status_player;

	@FXML
	private BottomBar bottomBar;

	@FXML
	private ImageView player_image;
	@FXML
	private ColorAdjust playerColorAdjust;
	@FXML
	private ImageView opponent_image;
	@FXML
	private ColorAdjust opponentColorAdjust;

	private Player playerPlayer;
	private ArrayList<Pokemon> playerPokemons;
	private Pokemon playerCurrent;
	private Player opponentPlayer;
	private ArrayList<Pokemon> opponentPokemons;
	private Pokemon opponentCurrent;

	private ArrayList<Pokemon> toWinPokemons;

	private int playerAcc;
	private int opponentAcc;

	private Move opponentHoldMove;

	private Queue<AttackDefenceRequest> attackDefenceQueue;

	private MapScene.SavedMapState savedMapState;


	public BattleScene(Player playerPlayer, ArrayList<Pokemon> playerpokemons, Player opponentPlayer, ArrayList<Pokemon> opponentpokemons, boolean isWild, MapScene.SavedMapState state) throws IOException {
		this.playerPlayer = playerPlayer;
		this.playerPokemons = (ArrayList<Pokemon>) playerpokemons.stream().map(Pokemon::clone).collect(toList());
		this.opponentPlayer = opponentPlayer;
		this.opponentPokemons = (ArrayList<Pokemon>) opponentpokemons.stream().map(Pokemon::clone).collect(toList());
		if ( isWild )
			toWinPokemons = (ArrayList<Pokemon>) opponentpokemons.stream().map(Pokemon::clone).collect(toList());
		this.savedMapState = state;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("battle_scene.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		loader.load();

		attackDefenceQueue = new LinkedList<>();

		initHandler();
		switchPokemon(Who.PLAYER, SwitchPokemon.SwitchMode.START, 0, r -> {
			switchPokemon(Who.OPPONENT, SwitchPokemon.SwitchMode.START, 0, r2 -> {
				loop();
			});
		});
	}


	public BattleScene(Player playerPlayer, Pokemon[] playerpokemons, Player opponentPlayer, Pokemon[] opponentpokemons, boolean isWild, MapScene.SavedMapState state) throws IOException {
		this(playerPlayer, new ArrayList<>(Arrays.asList(playerpokemons)), opponentPlayer, new ArrayList<>(Arrays.asList(opponentpokemons)), isWild, state);
	}

	@FXML
	private void initialize() {
		initSizes();
//		status_player.layout();
		this.layout();
		double status_playerY = Constrains.ROOT_HEIGHT - MARGIN_SIZE - STATUS_HEIGHT - BOTTOM_HEIGHT - 20,
				status_playerX = Constrains.ROOT_WIDTH - MARGIN_SIZE - STATUS_WIDTH,
				status_opponentY = MARGIN_SIZE,
				status_opponentX = MARGIN_SIZE;

		status_opponent.setLayoutX(status_opponentX);
		status_opponent.setLayoutY(status_opponentY);
		status_player.setLayoutX(status_playerX);
		status_player.setLayoutY(status_playerY);
		bottomBar.setLayoutY(Constrains.ROOT_HEIGHT - BOTTOM_HEIGHT);

		root.setBackground(new Background(new BackgroundImage(
				new Image(ImageDB.BG[0]),
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(Constrains.ROOT_WIDTH, Constrains.ROOT_HEIGHT, false, false, false, false)
		)));
	}

	private void initSizes() {

		root.setPrefHeight(Constrains.ROOT_HEIGHT);
		root.setPrefWidth(Constrains.ROOT_WIDTH);
		status_pane.setPrefHeight(Constrains.ROOT_HEIGHT);
		status_pane.setPrefWidth(Constrains.ROOT_WIDTH);

	}

	public void addChild(Node node) {
		this.normal_pane.getChildren().add(node);
	}


	private void initHandler() {
		this.bottomBar.setFightAction(event -> fight());
		this.bottomBar.setRunAction(event -> run());
		this.bottomBar.setPokemonAction(event -> changePokemon());
	}

	private void loop() {
		while ( attackDefenceQueue.peek() == null )
			startBattle();

		runBattle();
	}

	private void runBattle() {
		status_opponent.setAccumulator(opponentAcc / 100.0);
		status_player.setAccumulator(playerAcc / 100.0);
		if ( attackDefenceQueue.poll().who == Who.OPPONENT ) {
			if ( opponentCurrent.getHp() == 0 ) {
				switchPokemon(Who.OPPONENT, SwitchPokemon.SwitchMode.DIED, 0, result -> {
					if ( result ) {
						status_opponent.reFillAccumulator(() -> {
							opponentHoldMove = BattleLogic.getOpponentMove(opponentCurrent);
							speak(String.format("%s's %s used %s", opponentPlayer.getName(), opponentCurrent.getName(), opponentHoldMove.getName()), () -> {
								attack(new AttackDefence(opponentHoldMove, opponentCurrent.getAttack(), playerCurrent), Who.OPPONENT, () -> {
									if ( playerCurrent.getHp() == 0 ) {
										switchPokemon(Who.PLAYER, SwitchPokemon.SwitchMode.DIED, 0, result2 -> {
											if ( !result2 ) {
												win(Who.OPPONENT);
											} else {
												status_player.setAccumulator(playerAcc, this::loop);
											}
										});
									} else {
										status_player.setAccumulator(playerAcc, this::loop);
									}
								});
							});
						});
					} else {
						win(Who.PLAYER);
					}
				});
			} else {
				status_opponent.reFillAccumulator(() -> {
					opponentHoldMove = BattleLogic.getOpponentMove(opponentCurrent);
					speak(String.format("%s's %s used %s", opponentPlayer.getName(), opponentCurrent.getName(), opponentHoldMove.getName()), () -> {
						attack(new AttackDefence(opponentHoldMove, opponentCurrent.getAttack(), playerCurrent), Who.OPPONENT, () -> {
							status_player.setAccumulator(playerAcc, this::loop);
						});
					});
				});
			}


		} else {
			if ( playerCurrent.getHp() == 0 ) {
				switchPokemon(Who.PLAYER, SwitchPokemon.SwitchMode.DIED, 0, result -> {
					if ( result ) {
						if ( opponentCurrent.getHp() == 0 ) {
							switchPokemon(Who.OPPONENT, SwitchPokemon.SwitchMode.DIED, 0, result2 -> {
								if ( result2 ) {
									status_player.setAccumulator(1.0, () -> {
										speak(String.format("What will %s do?", playerCurrent.getName()), this::showControls);
									});
								} else {
									win(Who.PLAYER);
								}
							});
						} else {
							status_opponent.setAccumulator(opponentAcc, () -> {
								status_player.setAccumulator(1.0, () -> {
									speak(String.format("What will %s do?", playerCurrent.getName()), this::showControls);
								});
							});
						}
					} else {
						win(Who.OPPONENT);
					}
				});
			} else {
				System.out.println(opponentAcc);
				status_opponent.setAccumulator(opponentAcc, () -> {
					status_player.setAccumulator(1.0, () -> {
						speak(String.format("What will %s do?", playerCurrent.getName()), this::showControls);
					});
				});
			}
		}
	}

	private void startBattle() {
		int playerSpeed = playerCurrent.getSpeed();
		int opponentSpeed = opponentCurrent.getSpeed();
		playerAcc += playerSpeed;
		opponentAcc += opponentSpeed;

		if ( playerSpeed > opponentSpeed ) {
			while ( playerAcc >= 100 ) {
				playerAcc -= 100;
				attackDefenceQueue.add(new AttackDefenceRequest(Who.PLAYER));
			}
			while ( opponentAcc >= 100 ) {
				opponentAcc -= 100;
				attackDefenceQueue.add(new AttackDefenceRequest(Who.OPPONENT));
			}
		} else {
			while ( opponentAcc >= 100 ) {
				opponentAcc -= 100;
				attackDefenceQueue.add(new AttackDefenceRequest(Who.OPPONENT));
			}
			while ( playerAcc >= 100 ) {
				playerAcc -= 100;
				attackDefenceQueue.add(new AttackDefenceRequest(Who.PLAYER));
			}
		}

	}

	private void win(Who who) {
		if ( who == Who.PLAYER ) {
			String s;
			if ( toWinPokemons != null ) {
				s = String.format("and a set of %d pokemons", toWinPokemons.size());
				playerPlayer.getPokemons().addAll(toWinPokemons);
			} else {
				s = "";
			}
			speak(String.format("%s defated %s", playerPlayer.getName(), opponentPlayer.getName()), () -> {
				speak(String.format("%s got %d xp for winning %s", playerPlayer.getName(), EXP_ON_WIN, s), () -> {
					playerPlayer.addExp(EXP_ON_WIN);
					exitBattle();
				});
			});
		} else {
			speak(String.format("%s defated %s", opponentPlayer.getName(), playerPlayer.getName()), () -> {
				speak(String.format("%s lost and got nothing", playerPlayer.getName()), this::exitBattle);
			});
		}
	}

	private void switchPokemon(Who who, SwitchPokemon.SwitchMode mode, Pokemon pokemon, Callback.WithArg<Boolean> callable) {
		switch ( mode ) {
			case SET:
				if ( who == Who.PLAYER ) {
					unsummonPlayer(() -> {
						playerCurrent = pokemon;
						summonPlayer(() -> callable.call(true));
					});
				} else {
					unsummonOpponent(() -> {
						opponentCurrent = pokemon;
						summonOpponent(() -> callable.call(true));

					});
				}
				break;
			case DIED:
				if ( who == Who.PLAYER ) {
					speak(String.format("%s has fainted!", playerCurrent.getName()), () -> {
						playerPokemons.remove(playerCurrent);
						unsummonPlayer(() -> {
							if ( playerPokemons.isEmpty() ) {
								callable.call(false);
							} else {
								playerCurrent = playerPokemons.get(0);
								summonPlayer(() -> callable.call(true));
							}
						});
					});
				} else {
					speak(String.format("%s's %s has fainted!", opponentPlayer.getName(), opponentCurrent.getName()), () -> {
						opponentPokemons.remove(opponentCurrent);
						unsummonOpponent(() -> {
							if ( opponentPokemons.isEmpty() ) {
								callable.call(false);
							} else {
								opponentCurrent = opponentPokemons.get(0);
								summonOpponent(() -> callable.call(true));
							}
						});
					});
				}
				break;
			case START:
				if ( who == Who.PLAYER ) {
					playerCurrent = playerPokemons.get(0);
					summonPlayer(() -> callable.call(true));
				} else {
					opponentCurrent = opponentPokemons.get(0);
					summonOpponent(() -> callable.call(true));
				}
				break;
			default:
				throw new IllegalArgumentException(mode + " is not a valid type in BattleScene::switchPokemon");
		}
	}

	private void switchPokemon(Who who, SwitchPokemon.SwitchMode mode, int i, Callback.WithArg<Boolean> callable) {
		if ( who == Who.PLAYER ) {
			if ( i < playerPokemons.size() ) {
				switchPokemon(who, mode, playerPokemons.get(i), callable);
			} else {
				callable.call(false);
			}
		} else {
			if ( i < opponentPokemons.size() ) {
				switchPokemon(who, mode, opponentPokemons.get(i), callable);
			} else {
				callable.call(false);
			}
		}
	}

	private void unsummonOpponent(Callback callable) {
		System.out.println("unsummonOpponent");
		SwitchPokemon.animateSwitch(opponent_image, opponentColorAdjust, opponentCurrent.getFrontImg(), Who.OPPONENT, SwitchPokemon.SwitchMode.EXIT, callable);
	}

	private void unsummonPlayer(Callback callable) {
		System.out.println("unsummonPlayer");
		SwitchPokemon.animateSwitch(player_image, playerColorAdjust, playerCurrent.getBackImg(), Who.PLAYER, SwitchPokemon.SwitchMode.EXIT, callable);
	}

	private void summonOpponent(Callback callable) {
		System.out.println("summonOpponent");
		status_opponent.setFull_hp(opponentCurrent.getFullHp());
		status_opponent.setHealth(opponentCurrent.getHp());
		status_opponent.setVisible(true);
		status_opponent.setName(opponentCurrent.getName());
		SwitchPokemon.animateSwitch(opponent_image, opponentColorAdjust, opponentCurrent.getFrontImg(), Who.OPPONENT, SwitchPokemon.SwitchMode.ENTER, callable);
	}

	private void summonPlayer(Callback callable) {
		System.out.println("summonPlayer");
		status_player.setFull_hp(playerCurrent.getFullHp());
		status_player.setHealth(playerCurrent.getHp());
		status_player.setVisible(true);
		status_player.setName(playerCurrent.getName());
		SwitchPokemon.animateSwitch(player_image, playerColorAdjust, playerCurrent.getBackImg(), Who.PLAYER, SwitchPokemon.SwitchMode.ENTER, callable);
	}

	private void changePokemon() {
		BottomBar.MoveHandler[] handlers = new BottomBar.MoveHandler[4];

		for ( int i = 0; i < playerPokemons.size(); i++ ) {
			Pokemon p = playerPokemons.get(i);
			handlers[i] = new BottomBar.MoveHandler(p.getName(), "", () -> {
				this.bottomBar.setMovesVisibile(false);
				switchPokemon(Who.PLAYER, SwitchPokemon.SwitchMode.SET, p, result -> {
					if ( result ) {
						loop();
					} else {
						System.out.println("Error");
					}
				});
				( (LinkedList<AttackDefenceRequest>) attackDefenceQueue ).add(0, new AttackDefenceRequest(Who.PLAYER));
			});
		}


		for ( int i = playerPokemons.size(); i < 4; i++ ) {
			handlers[i] = new BottomBar.MoveHandler("", "", () -> {
			});
		}


		this.bottomBar.setMoves(handlers);
		this.bottomBar.setMovesVisibile(true);
		hideControls();
	}

	private void run() {
		//todo: implement this after map(exit from this scene)
		speak(String.format("%s is trying to escape", playerPlayer.getName()), () -> exitBattle());
	}

	private void exitBattle() {
		try {
			if ( !Music.isMute )
				Music.getPlayers().get(Music.Place.BATTLE).stop();
			root.getScene().setRoot(new MapScene(playerPlayer, savedMapState, playerPlayer.getPokemonsForMe()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void fight() {
		BottomBar.MoveHandler[] handlers = new BottomBar.MoveHandler[4];
		Pokemon attacking, defending;
		attacking = playerCurrent;
		defending = opponentCurrent;
		Move[] moves = attacking.getMoves();
		assert moves.length == 4;

		for ( int i = 0; i < 4; i++ ) {
			final AttackDefence attackDefence = new AttackDefence(moves[i], attacking.getAttack(), defending);
			handlers[i] = new BottomBar.MoveHandler(moves[i].getName(), moves[i].getType(), () -> {
				this.playerAttack(attackDefence, this::loop);
				this.bottomBar.setMovesVisibile(false);
			});
		}


		this.bottomBar.setMoves(handlers);
		this.bottomBar.setMovesVisibile(true);
		hideControls();
	}

	private void playerAttack(AttackDefence attackDefence, Callback callable) {
		speak(String.format("%s used %s", playerCurrent.getName(), attackDefence.attackingMove.getName()), () -> {
			attack(attackDefence, Who.PLAYER, callable);
		});

	}

	private void attack(AttackDefence attackDefence, Who attacking) {
		this.attack(attackDefence, attacking, () -> {
		});
	}

	private void attack(AttackDefence attackDefence, Who attacking, Callback callable) {
		BattleLogic.AttackResult result = BattleLogic.attack(attackDefence.defendingPokemon, attackDefence.attackingPower, attackDefence.attackingMove);

		ImageView attimg, defimg;

		if ( attacking == Who.PLAYER ) {
			attimg = player_image;
			defimg = opponent_image;
		} else {
			attimg = opponent_image;
			defimg = player_image;
		}

		Utils.attackEffect(defimg, attimg, attacking, () -> {
		}, result.getStatus() == BattleLogic.AttackStatus.MISS);

		if ( attacking == Who.PLAYER ) {
			Callback myCallable = () -> {
				opponentCurrent.setHp(result.getNewHP());
				status_opponent.setHealth(result.getNewHP(), callable);
			};
			switch ( result.getStatus() ) {
				case SUPER_EFFECTIVE:
					speak("It's super effective!", myCallable);
					break;
				case MISS:
					speak("Missed!", myCallable);
					break;
				case NOT_VERY_EFFECTIVE:
					speak("It's not very effective...", myCallable);
					break;
				case NOT_EFFECTIVE:
					speak("It's not effective at all!", myCallable);
					break;
				default:
					myCallable.call();
					break;
			}
		} else {
			playerCurrent.setHp(result.getNewHP());
			status_player.setHealth(result.getNewHP(), callable);
		}
	}

	private boolean speak(String s, Callback callable) {
		if ( s == null || callable == null )
			return false;
		return this.bottomBar.setText(s, callable);
	}

	private void showControls() {
		this.bottomBar.setControlsVisibile(true);
	}

	private void hideControls() {
		this.bottomBar.setControlsVisibile(false);
	}

	private class AttackDefenceRequest {
		Who who;

		private AttackDefenceRequest(Who who) {
			this.who = who;
		}
	}

	private class AttackDefence {
		private Move attackingMove;
		private int attackingPower;
		private Pokemon defendingPokemon;

		AttackDefence(Move attackingMove, int attackingPower, Pokemon defendingPokemon) {
			this.attackingMove = attackingMove;
			this.attackingPower = attackingPower;
			this.defendingPokemon = defendingPokemon;
		}
	}
}
