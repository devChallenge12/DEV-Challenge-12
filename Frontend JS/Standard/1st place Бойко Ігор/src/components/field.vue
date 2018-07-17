<template>
	<div>
		<!-- <div class="game-menu" v-if="isShowMenu">
			<button @click="setGameMode('player')">player vs player</button>
			<button @click="setGameMode('computer')">player vs computer</button>
		</div> -->
		<div v-if="isShowWinner">
			Победитель {{winner}}
		</div>
		<div class="field" v-if="!isShowMenu && !isShowWinner">
			<div class="currentPlayer"> {{currentPlayer}}</div>
			<div class="playerPoints">
				<div> броски {{gameCount}}/16</div>
				<div>
					player1: {{player1.points}}
				</div>
				<div>
					player2: {{player2.points}}
				</div>
			</div>
			<div class="center-line"></div>
			<div class="out"></div>
			<div class="circle">
				<div class="house">
					<div class="center"></div>
				</div>
			</div>
			<div class="start-block">
				<div class="angle-btn less" @click="changeAngle('less')">less</div>
				<div class="angle-btn more" @click="changeAngle('more')">more</div>
				<div class="arrow" v-bind:style="arrowAngle"></div>
				<div class="rock-starter">
					<div class="rock stopped" v-for="(rock, index) in rocks" :key="index" @click="startRide($event.target)" v-bind:class="{'red': rock.currentPlayer == 'player1', 'blue': rock.currentPlayer == 'player2'}" :currentPlayer="rock.currentPlayer"></div>
				</div>
			</div>
			<div class="power">
				<div class="power-count" v-for="item in [1,2,3,4,5,6,7,8]" :key="item" @click="selectPower(item)" v-bind:class="{'active': power == item}"> {{item}}</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'field',
		components: {},
		computed: {
			arrowAngle(){
				return {transform : `rotate(${this.rotateAngle}deg)`}
			}
		},
		data() {
			return {
				gameCount: 0,
				isShowMenu: false,
				isShowWinner: false,
				winner: null,
				gameMode: null,
				rotateAngle: 0,
				power: 1,
				rocks: [
					{
						currentPlayer: 'player1'
					}
				],
				currentPlayer: 'player1',
				intervalId: null,
				punchCounter: 0,
				player1: {
					points: 0
				},
				player2: {
					points: 0
				}
			}
		},
		methods: {
			setGameMode(mode) {
				this.gameMode = mode;
				this.isShowMenu = false;
			},
			changeAngle(type){
				type == 'more' ? this.rotateAngle++ : this.rotateAngle--;
			},
			startRide(elem) {
				this.gameCount += 1;
				const rideWay = $('.field').height() / 8.5;
				$(elem).addClass('active');
				$(elem).removeClass('stopped');
				let positionValue;
				switch (true) {
					case (this.rotateAngle == 0): {
						positionValue = undefined;
						break
					}
					case (this.rotateAngle < 0): {
						positionValue = (94 - Math.tan(-this.rotateAngle* Math.PI/180)*(rideWay*this.power)) + 'px';
						break
					}
					case (this.rotateAngle > 0): {
						positionValue = (94 + Math.tan(this.rotateAngle* Math.PI/180)*(rideWay*this.power)) + 'px';
						break
					}
				}
				this.watchRocks();
				$(elem).animate({
						bottom: rideWay * this.power + 'px',
						left: positionValue
					},
					{
						duration: 1000,
						easing: "easeOutCubic",
						complete: () => {
							$(elem).removeClass('active');
							$(elem).addClass('stopped');
							this.stopWatchRocks();
							this[this.currentPlayer].points += 1;
							this.addNewElement();
							this.changeplayer();
							this.checkCollisionElementOnStart(elem);
							this.checkWinner();
					}
				});
			},
			punchAnimation(elem, power){
				const bottomPosition = $('.rock.active').css('bottom');
	        	const leftPosition = $('.rock.active').css('left');
	        	$('.rock.active').stop().css('bottom', bottomPosition, 'left', leftPosition);
	        	$('.rock.active').addClass('stopped');
	        	$('.rock.active').removeClass('active');
	        	this.startPunchRide(elem, power);
			},
			startPunchRide(elem, power){
				const rideWay = $('.field').height() / 8;
				$(elem).addClass('active');
				$(elem).removeClass('stopped');
				let positionValue;
				switch (true) {
					case (this.rotateAngle == 0): {
						positionValue = undefined;
						break
					}
					case (this.rotateAngle < 0): {
						positionValue = (94 - Math.tan(-this.rotateAngle* Math.PI/180)*(rideWay*this.power)) + 'px';
						break
					}
					case (this.rotateAngle > 0): {
						positionValue = (94 + Math.tan(this.rotateAngle* Math.PI/180)*(rideWay*this.power)) + 'px';
						break
					}
				}
				this.watchRocks();
				const bottom = +$(elem).css('bottom').replace('px','').trim();
				this.punchCounter++;
				$(elem).animate({
						bottom: bottom + (power/5) + 'px',
						left: positionValue
					},
					{
						duration: 1000,
						easing: "easeOutCubic",
						complete: () => {
							$(elem).removeClass('active');
							$(elem).addClass('stopped');
							this.stopWatchRocks();
							this[this.currentPlayer].points += 1;
							this.addNewElement();
							this.changeplayer();
							this.punchCounter = 0;
							this.checkWinner();
					}
				});
			},
			changeplayer(){
				this.currentPlayer = this.currentPlayer == 'player1' ? 'player2' : 'player1';
			},
			addNewElement(){
				const rock = {};
				rock.currentPlayer = this.currentPlayer == 'player1' ? 'player2' : 'player1';
				this.rocks.push(rock);
			},
			selectPower(power){
				this.power = power;
			},
			watchRocks(){
				this.intervalId2 = setInterval(()=> {
					this.checkCollisionOutBlock($('.rock.active'));
				},50)
				if($('.rock').length == 1) {
					return
				}
				this.intervalId = setInterval(()=> {
					this.checkCollisionOutBlock($('.rock'));
					$('.stopped').each((index, element) => {
						this.checkCollision($('.rock.active'), $('.stopped:not(d-none)')[index], index);
					})
				},50)
			},
			checkCollision(elem1, elem2){
				var x1 = $(elem1).offset().left;
		        var y1 = $(elem1).offset().top;
		        var h1 = $(elem1).outerHeight(true);
		        var w1 = $(elem1).outerWidth(true);
		        var b1 = y1 + h1;
		        var r1 = x1 + w1;
		        var x2 = $(elem2).offset().left;
		        var y2 = $(elem2).offset().top;
		        var h2 = $(elem2).outerHeight(true);
		        var w2 = $(elem2).outerWidth(true);
		        var b2 = y2 + h2;
		        var r2 = x2 + w2;
		        if (b1 < y2 || y1 > b2 || r1 < x2 || x1 > r2) {
		        	// console.log(false)
		        }else{
		        	this.stopWatchRocks();
		        	const power = (+$(elem1).css('bottom').replace('px', '').trim() * (this.power / 2));
		        	this.punchAnimation(elem2, power);
		        }
			},
			checkCollisionElementOnStart(elem1){
				var x1 = $(elem1).offset().left;
		        var y1 = $(elem1).offset().top;
		        var h1 = $(elem1).outerHeight(true);
		        var w1 = $(elem1).outerWidth(true);
		        var b1 = y1 + h1;
		        var r1 = x1 + w1;
		        var x2 = $('.start-block').offset().left;
		        var y2 = $('.start-block').offset().top;
		        var h2 = $('.start-block').outerHeight(true);
		        var w2 = $('.start-block').outerWidth(true);
		        var b2 = y2 + h2;
		        var r2 = x2 + w2;
		        if (b1 < y2 || y1 > b2 || r1 < x2 || x1 > r2) {
		        	// console.log(false)
		        }else{
		        	$(elem1).addClass('d-none')
		        }
			},
			checkCollisionOutBlock(elem1){
				var x1 = $(elem1).offset().left;
		        var y1 = $(elem1).offset().top;
		        var h1 = $(elem1).outerHeight(true);
		        var w1 = $(elem1).outerWidth(true);
		        var b1 = y1 + h1;
		        var r1 = x1 + w1;
		        var x2 = $('.out').offset().left;
		        var y2 = $('.out').offset().top;
		        var h2 = $('.out').outerHeight(true);
		        var w2 = $('.out').outerWidth(true);
		        var b2 = y2 + h2;
		        var r2 = x2 + w2;
		        if (b1 < y2 || y1 > b2 || r1 < x2 || x1 > r2) {
		        	// console.log(false)
		        }else{
		        	$(elem1).addClass('d-none');
		        	this[$(elem1).attr('currentPlayer')].points -=1;
		        	this.stopWatchRocks();
		        }
			},
			stopWatchRocks() {
				clearInterval(this.intervalId);
				clearInterval(this.intervalId2);
			},
			checkWinner(){
				if( this.gameCount == 16) {
					this.winner = this.currentPlayer;
					this.isShowWinner = true;
				}
			}
		}
	}
</script>

<style lang="scss">
	.field{
		height: 100vh;
		width: 220px;
		border: solid 1px black;
		margin: auto;
		display: inline-block;
		position: relative;
		text-align: center;
		.center-line{
			width: 1px;
			background: black;
			bottom: 120px;
			top: 0;
			left: 50%;
			transform: translateX(-50%);
			position: absolute;
		}
		.out{
			height: 10vh;
			border-bottom: solid 1px black;
		}
		.circle{
			height: 180px;
			width: 180px;
			display: inline-block;
			position: relative;
			.house{
				border: solid 30px red;
				width: 100%;
				height: 100%;
				border-radius: 50%;
				display: flex;
				justify-content: center;
				align-items: center;
			}
			.center{
				border: solid 15px blue;
				border-radius: 50%;
				width: 60px;
				height: 60px;
			}
		}
		.start-block{
			height: 150px;
			position: absolute;
			bottom: 0;
			left: 0;
			right: 0;
			border-top: solid 1px black;
		}
		.rock-starter{
			position: absolute;
			bottom: 0;
			left: 0;
			right: 0;
			display: flex;
			justify-content: center;
		}
		.rock{
			width: 30px;
			height: 30px;
			border-radius: 50%;
			position: absolute;
			left: 94px;
			z-index: 997;
			border: solid 2px black;
			bottom: 10px;
			&.red{
				background: red;
			}
			&.blue{
				background: blue;
			}
			.d-none{
				display: none;
			}
			&:hover{
				cursor: pointer;
			}
		}
		.arrow{
			height: 100px;
			width: 1px;
			background: black;
			position: absolute;
			bottom: 0;
			left: 50%;
			transform-origin: bottom;
			&:before,
			&:after{
				content: '';
				position: absolute;
				top: 0;
				width: 10px;
				height: 1px;
				background: black;
			}
			&:after{
				right: 0;
				transform-origin: top right;
				transform: rotate(-45deg)
			}
			&:before{
				transform-origin: top left;
				transform: rotate(45deg)
			}
		}
		.angle-btn{
			position: absolute;
			top: 10px;
			&:hover{
				cursor: pointer;
			}
			&.less{
				left: 8px;
			}
			&.more{
				right: 8px;
			}
		}
		.power{
			&:before{
				content: 'Power';
				position: absolute;
				top: -24px;
			}
			position: absolute;
			right: -40px;
			width: 40px;
			height: 150px;
			bottom: 0;
			display: flex;
			flex-direction: column-reverse;
			border: solid 1px black;
			.power-count{
				text-align: center;
				font-size: 12px;
				&:hover{
					cursor: pointer;
				}
				&.active{
					font-size: 14px;
					font-weight: bold;
					background: red;
				}
			}
		}
		.currentPlayer{
			position: absolute;
			top: 0;
			right: -100px;
			width: 100px;
		}
		.playerPoints{
			position: absolute;
			left: -100px;
			width: 100px;
		}
	}
</style>
