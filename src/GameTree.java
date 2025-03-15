import java.util.ArrayList;

public class GameTree {
    int player1Score; // Spēlētāja punkti katrā instancē kad notiks izmaiņa
    int player2Score; // Datora punkti katrā instancē kad notiks izmaiņa
    ArrayList<GameTree> children; // Koks kurš sastāv no bērna bērniem
    ArrayList<Integer> gameState; // Spēles instance katrā brīdi kad tiek izmainīts
    // Ideja ir ka mēs izmainam gameState nosakot kurus skaitļus mēs gribam mainīt skatoties pēc node index katrā līmenī
    // Mana ideja var būt ļoti atmiņu ziņā ļoti dārga
    boolean player1turn;

    public GameTree(int player1Score, int player2Score, ArrayList<Integer> gameState, boolean player1turn) {
        children = new ArrayList<>();
        this.gameState = new ArrayList<>(gameState);
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.player1turn = player1turn;
    }

    public void generateChildren(int maxDepth, int currentDepth){
        if(currentDepth >= maxDepth || isTerminal()) return;
        for(int i = 0; i <gameState.size() - 1; i++){
            ArrayList<Integer> newGameState = new ArrayList<>(gameState);
            int a = gameState.get(i), b = gameState.get(i+1);
            int sum = a+b, newScore1= player1Score, newScore2=player2Score;
            int replacement;

            if(sum>7){
                replacement = 1;
                if(player1turn) newScore1+=1;
                else newScore2+=1;
            }else if(sum<7){
                replacement = 3;
                if(player1turn) newScore1-=1;
                else newScore2-=1;
            }else{
                replacement=2;
                if(player1turn) newScore1+=2;
                else newScore2+=2;
            }

            newGameState.set(i, replacement);
            newGameState.remove(i+1);

            GameTree child = new GameTree(newScore1,newScore2,newGameState, !player1turn);
            children.add(child);

            child.generateChildren(maxDepth, currentDepth + 1);
        }
    }


    public ArrayList<GameTree> getChildren() {
        return children;
    }


    public boolean isTerminal(){
    return gameState.size()==1;
    }


//testesanai parada visus iespejamus stavoklus 1 un 2 limeni 
    public void printTree(int depth){
            String indent = " ".repeat(depth*2);
            System.out.println(indent + "stavoklis: " + gameState + " |P1 " + player1Score + " |P2 " + player2Score);
            for(GameTree child : getChildren()){
                child.printTree(depth +1);
            }
    }

    //heiristiska?? idk neko vairak neizdomaju
    public int heuristic(){
        int count7 =0;
        int over7 = 0;
        int below7 = 0;

        for(int i = 0; i<gameState.size()-1; i++){
            int sum = gameState.get(i) + gameState.get(i +1);

            if(sum==7)count7++;
            else if(sum>7) over7++;
            else below7++;
        }

        return 2*count7 + over7 - below7;//izsauksim lai apskatitos preferable gamestate???
    }

}

