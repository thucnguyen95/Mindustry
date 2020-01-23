package mindustry.ecs;

import arc.ecs.*;
import arc.func.*;
import arc.graphics.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.ArcAnnotate.*;
import mindustry.entities.Effects.*;
import mindustry.entities.traits.BuilderTrait.*;
import mindustry.entities.traits.*;
import mindustry.game.*;
import mindustry.net.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.BuildBlock.*;

import static mindustry.Vars.*;

public class Components{

    public static class Healthc extends Component{
        public float health;
        public boolean dead;
    }

    public static class Hitboxc extends Component{
        public float width, height;
    }

    public static class Posc extends Component implements Position{
        public float x, y;

        public Posc set(float x, float y){
            this.x = x;
            this.y = y;
            return this;
        }

        @Override
        public float getX(){
            return x;
        }

        @Override
        public float getY(){
            return y;
        }
    }

    public static class Rotc extends Component{
        public float rot;
    }

    public static class Velocityc extends Component{
        public Vec2 vec = new Vec2();
        public float drag = 0f;
    }

    public static class Teamc extends Component{
        public Team team = Team.derelict;
    }

    public static class Minec extends Component{
        public Tile tile;
        public float speed = 1f;
        public boolean autounload = false;
        public Boolf<Item> canMine = i -> false;

        public boolean mines(Item item){
            return canMine.get(item);
        }
    }

    public static class Buildc extends Component{
        public Queue<BuildRequest> requests = new Queue<>();
        public float speed = 1f;
        public boolean building;

        public void remove(int x, int y, boolean breaking){
            //remove matching request
            int idx = player.buildQueue().indexOf(req -> req.breaking == breaking && req.x == x && req.y == y);
            if(idx != -1){
                player.buildQueue().removeIndex(idx);
            }
        }

        /** Return whether this builder's place queue contains items. */
        public boolean building(){
            return requests.size != 0;
        }

        /** Clears the placement queue. */
        public void clear(){
            requests.clear();
        }

        /** Add another build requests to the tail of the queue, if it doesn't exist there yet. */
        public void add(BuildRequest place){
            add(place, true);
        }

        /** Add another build requests to the queue, if it doesn't exist there yet. */
        public void add(BuildRequest place, boolean tail){
            BuildRequest replace = null;
            for(BuildRequest request : requests){
                if(request.x == place.x && request.y == place.y){
                    replace = request;
                    break;
                }
            }
            if(replace != null){
                requests.remove(replace);
            }
            Tile tile = world.tile(place.x, place.y);
            if(tile != null && tile.entity instanceof BuildEntity){
                place.progress = tile.<BuildEntity>ent().progress;
            }
            if(tail){
                requests.addLast(place);
            }else{
                requests.addFirst(place);
            }
        }

        /** Return the build requests currently active, or the one at the top of the queue.*/
        public @Nullable BuildRequest curr(){
            return requests.size == 0 ? null : requests.first();
        }
    }

    public static class Syncc extends Component{
        //network ID
        public int id;
    }

    public static class Effectc extends Component{
        public Effect effect;
    }

    public static class Flyingc extends Component{
        public boolean achievedFlight;
        public boolean flying;
    }

    public static class Boostc extends Component{
        public float boostHeat;
    }

    public static class Legsc extends Component{
        public float rotation;
    }

    public static class Spawningc extends Component{
        public SpawnerTrait spawner, lastSpawner;
    }

    public static class Itemc extends Component{
        public ItemStack stack = new ItemStack();
        public int capacity;

        public void clear(){
            stack.amount = 0;
        }

        public boolean accepts(Item item){
            return empty() || item == stack.item && stack.amount + 1 <= capacity;
        }

        public boolean empty(){
            return stack.amount == 0;
        }
    }

    public static class Statusesc extends Component{
        public Array<StatusEntry> list = new Array<>();

        public static class StatusEntry{
            public StatusEffect effect;
            public float time;

            public StatusEntry set(StatusEffect effect, float time){
                this.effect = effect;
                this.time = time;
                return this;
            }
        }
    }

    public static class Shellc extends Component{
        //insert unit type / mech here, for the mech-rework branch
    }

    public static class Drawc extends Component{
        //???
    }

    public static class Playerc extends Component{
        public String name = "noname";
        public @Nullable String uuid, usid;
        public boolean isAdmin, isTransferring, isShooting, isBoosting, isMobile, isTyping;
        public float destructTime;
        public Color color = new Color();

        public @Nullable NetConnection con;
        public boolean isLocal = false;

        public @Nullable String lastText;
        public float textFadeTime;
    }

}