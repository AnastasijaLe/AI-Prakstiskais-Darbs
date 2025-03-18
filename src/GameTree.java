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
            int heuristicValue=heuristic();
            int minimaxValue = minmax(this, 2, player1turn); 
            System.out.println(indent + "stavoklis: " + gameState + " |P1 " + player1Score + " |P2 " + player2Score + " | Heurisrtic " + heuristicValue + "| MinMax " + minimaxValue);
            for(GameTree child : getChildren()){
                child.printTree(depth +1);
            }
    }
           
    public int heuristic(){
        if(player1Score>player2Score) return 1;
        if(player1Score<player2Score) return -1;

        return 0;
    }

    public int minmax(GameTree node, int depth, boolean player1turn){
        int eval, min, max;
        if(depth == 0 || node.isTerminal() || node.getChildren().isEmpty()) return node.heuristic();

        if(player1turn){
            max = Integer.MIN_VALUE;
            for(GameTree child : node.getChildren()){
                eval = minmax(child, depth -1, false);
                max= Math.max(max, eval);
            }
            return max;
        }else{
            min = Integer.MAX_VALUE;
            for(GameTree child : node.getChildren()){
                eval = minmax(child, depth-1, true);
                min=Math.min(min, eval);
            }
            return min;
        }
    }

    public GameTree bestMove(int depth){
        if(this.getChildren().isEmpty() && !this.isTerminal()){
            this.generateChildren(depth, 0);
        }

        GameTree bestChild = null;
        int bestVal;

        if(player1turn){
            bestVal=Integer.MIN_VALUE;
            for(GameTree child: getChildren()){
                int value = minmax(child, depth-1, false);
                if(value>bestVal){
                    bestVal=value;
                    bestChild=child;
                }
            }
        }else{
            bestVal=Integer.MAX_VALUE;
            for(GameTree child: getChildren()){
                int value = minmax(child, depth-1, true);
                if(value>bestVal){
                    bestVal=value;
                    bestChild=child;
                 }
             }
        }
        return bestChild;
    }
}