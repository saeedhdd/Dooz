package com.example.hd.dooz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {
    static final int CROSS_PLAYER = R.drawable.cross;
    static final int CIRCLE_PLAYER = R.drawable.circle;
    static final int EMPTY = 0;
    private static final long START_DELAY_COMPUTER = 300 ;
    private static final long SFIRST_TIME_START_DELAY_COMPUTER = 0 ;
    private static final long ANIMATION_DURATION_NEW_MOHRE_COMPUTER = 500;
    private static final long WINING_HIGHLIT_DURATION = 500;
    private static final long WINING_HIGHLIT_START_DELAY = 200;
    int[] status = new int[9];
    int[] ids = {R.id.iv0,R.id.iv1,R.id.iv2,R.id.iv3,R.id.iv4,R.id.iv5,R.id.iv6,R.id.iv7,R.id.iv8};
    int humanPlayer = CROSS_PLAYER;
    int computerPlayer = CIRCLE_PLAYER;
    boolean firstMove = false;
    String playerName = "You";
    String computerPlayerName = "Computer";
    static final int [][] winStatus= {{0,1,2},{3,4,5},{6,7,8},{0,4,8},{2,4,6},{0,3,6},{1,4,7},{2,5,8}};
    int firstPlayer = CIRCLE_PLAYER;
//    int activePlayer = firstPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field);
        humanPlayer = (Integer) getIntent().getExtras().get("human");
        computerPlayer = humanPlayer==CROSS_PLAYER?CIRCLE_PLAYER:CROSS_PLAYER;
        ImageView yourMohre = (ImageView) findViewById(R.id.YourMohr);
        yourMohre.setImageResource(humanPlayer);
//        if (getSupportActionBar()!=null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
        start();
    }

    public void OnClick(View view){
        ImageView imageView = (ImageView) view;
        int tag = Integer.parseInt((String)view.getTag());
        if ( hasAnybodyWon()!=0|| status[tag]!=EMPTY){return;}
        status[tag] = humanPlayer;
        showImage(imageView, humanPlayer, 0);
        int winner = hasAnybodyWon();
        if (gameIsFinished(winner)){

            finishGame(winner);
            return;
        }else {
            computerTurn();
        }

//        activePlayer = activePlayer==CROSS_PLAYER?CIRCLE_PLAYER:CROSS_PLAYER;
    }
    TextView winnerTV;
    private void finishGame(int winner) {
        winnerTV = (TextView) findViewById(R.id.winnerTV);
        if (winner==0){
            winnerTV.setText( "مساوی!");
        }else {
            winnerTV.setText((winner == computerPlayer ? "شما باختید!" : "شما بردید!"));
            ((ImageView) (findViewById(ids[winStatus[winStateID][0]]))).animate().alpha(0.5f).scaleX(1.2f).scaleY(1.2f).setDuration(WINING_HIGHLIT_DURATION).setStartDelay(WINING_HIGHLIT_START_DELAY);
            ((ImageView) (findViewById(ids[winStatus[winStateID][1]]))).animate().alpha(0.5f).scaleX(1.2f).scaleY(1.2f).setDuration(WINING_HIGHLIT_DURATION).setStartDelay(WINING_HIGHLIT_START_DELAY);
            ((ImageView) (findViewById(ids[winStatus[winStateID][2]]))).animate().alpha(0.5f).scaleX(1.2f).scaleY(1.2f).setDuration(WINING_HIGHLIT_DURATION).setStartDelay(WINING_HIGHLIT_START_DELAY);
        }
            winnerTV.setScaleX(5);
            winnerTV.setScaleY(5);
            winnerTV.setAlpha(0);
            winnerTV.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(WINING_HIGHLIT_DURATION).setStartDelay(WINING_HIGHLIT_START_DELAY);
        }

    private boolean gameIsFinished(int winner){
        return noMoreFreeSpace()|| winner!=0;
    }
    private void fillPlace(int i) {
        status[i] = computerPlayer;
        ImageView iv = (ImageView) findViewById(ids[i]);
        if (firstMove) {
            showImage(iv, computerPlayer, SFIRST_TIME_START_DELAY_COMPUTER);
            firstMove=false;
        }else
            showImage(iv, computerPlayer, START_DELAY_COMPUTER);
    }

    private boolean finishingActions(){
        int winner = hasAnybodyWon();
        boolean f = gameIsFinished(winner);
        if (f) {
            finishGame(winner);
        }
        return f;
    }

    private void computerTurn() {
        int actionHouse = isPlayerAboutToWin(computerPlayer);
        if (actionHouse==-1) actionHouse=isPlayerAboutToWin(humanPlayer);
        if (actionHouse!= -1 ) {
            fillPlace(actionHouse);
            finishingActions();
            return;
        }
        if (firstPlayer!=computerPlayer){
            if (status[4]==EMPTY){
                fillPlace(4);
                finishingActions();
                return;
            }
        }else {
            int[][] roadMap = {{0,2,6}, {2,8,6},{8,6,0} , {0,2,8}};
            shuffleArray(roadMap);
            for (int i = 0; i < roadMap.length; i++) {
                int[] state =  {status[roadMap[i][0]],status[roadMap[i][1]], status[roadMap[i][2]]};
                if (state[0] == humanPlayer || state[1] == humanPlayer || state[2]== humanPlayer)
                    continue;
                boolean totallyEmpty = (state[0] == EMPTY && state[1] == EMPTY || state[2]== EMPTY);
                for (int j = 0; j < state.length; j++) {
                    if (state[j] == EMPTY) {
                        status[roadMap[i][j]] = computerPlayer;
                        if (totallyEmpty || isPlayerAboutToWin(computerPlayer)==computerPlayer){
                            fillPlace(roadMap[i][j]);
                            finishingActions();
                            return;
                        }else {
                            status[roadMap[i][j]] = EMPTY;
                            continue;
                        }
                    }
                }
            }

        }
            for (int i = 0; i < status.length ; i++) {
                if (status[i] == EMPTY){
                    fillPlace(i);
                    break;
                }
            }
            finishingActions();
            return;

    }

    private int isPlayerAboutToWin(int player) {
        int[][] human = {{player,player,EMPTY},{player,EMPTY,player}, {EMPTY,player,player}};
        for (int i = 0; i < winStatus.length ; i++) {
            int[] winCandidate = {status[winStatus[i][0]],status[winStatus[i][1]],status[winStatus[i][2]]};
            for (int j = 0; j < human.length; j++) {
                if      (winCandidate[0]== human[j][0] &&
                        winCandidate[1]==human[j][1] &&
                        winCandidate[2]==human[j][2]) {

                    if (winCandidate[0] == EMPTY) return winStatus[i][0];
                    if (winCandidate[1] == EMPTY) return winStatus[i][1];
                    if (winCandidate[2] == EMPTY) return winStatus[i][2];
                }
            }
        }
        return -1;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem reset = menu.add("تکرار");
        reset.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                resetGame();
                return false;
            }
        });
        reset.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    private void resetGame() {
        Arrays.fill(status,0);
        winnerTV = (TextView) findViewById(R.id.winnerTV);
        winnerTV.setText(" ");
        setFirstPlayer();
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_id);
        for (int i=0 ; i<layout.getChildCount();i++) {
            LinearLayout childLayout = (layout.getChildAt(i) instanceof LinearLayout)?(LinearLayout) layout.getChildAt(i):null;
            if (childLayout!=null){
                for (int j=0 ; j<childLayout.getChildCount();j++) {
                    ImageView iv = (childLayout.getChildAt(j) instanceof ImageView)?(ImageView) childLayout.getChildAt(j):null;
                    if (iv!=null)
                        iv.setAlpha(1f);
                        iv.setImageResource(0);
                }
            }
        }
        start();
    }

    private void start() {
        ImageView firsPlayerImage = (ImageView) findViewById(R.id.imageView);
        firsPlayerImage.setImageResource(firstPlayer);
        if (firstPlayer==computerPlayer){
            firstMove = true;
            computerTurn();
        }
    }

    private void setFirstPlayer() {
        firstPlayer = firstPlayer==CIRCLE_PLAYER?CROSS_PLAYER:CIRCLE_PLAYER;
    }
    int winStateID = 0;
    private int hasAnybodyWon() {
        for (int i = 0; i < winStatus.length ; i++) {
            if (status[winStatus[i][0]] == status[winStatus[i][1]]
                    && status[winStatus[i][2]] == status[winStatus[i][1]] && status[winStatus[i][0]] != EMPTY){
                winStateID = i;
                return status[winStatus[i][0]];
            }
        }
        return 0;
    }

    private boolean noMoreFreeSpace() {
        for (int i = 0; i < status.length; i++) {
            if (status[i]==EMPTY)return false;
        }
        return  true;
    }

    private void showImage(ImageView imageView, int Player, long delay) {
        imageView.setScaleX(0.01f);
        imageView.setScaleY(0.01f);
        imageView.setImageResource(Player);
        imageView.animate().scaleX(1f).scaleY(1f).setDuration(ANIMATION_DURATION_NEW_MOHRE_COMPUTER).setStartDelay(delay);
    }

    private static  <E> void  shuffleArray(E[] array)
    {
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            E temp = null;
            if (index != i)
            {
                temp = array[index];
                array[index] = array[i];
                array[i] = temp;
            }
        }
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId()==android.R.id.home){
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
