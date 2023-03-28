package es.mariaanasanz.ut7.mods.impl;

import ca.weblite.objc.Client;
import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.logging.XMLFormatter;

@Mod(DamMod.MOD_ID)
public class ExampleMod extends DamMod implements IBlockBreakEvent, IServerStartEvent,
        IItemPickupEvent, ILivingDamageEvent, IUseItemEvent, IFishedEvent,
        IInteractEvent, IMovementEvent {
    private Player jugador;
    private Level mundo;
    private boolean serverStarting = false;
    private BlockPos posicionJugador;
    private BlockPos posicionBloqueDebajo;
    private BlockPos posicioAnteriorJugador = new BlockPos(0, 0 ,0);
   // private BlockPos posicionBloqueAnterior;
    double PosXAnteriorJugador = Double.MAX_VALUE;
    double PosZAnteriorJugador = Double.MAX_VALUE;
    double PosXAnteriorBloque = Double.MAX_VALUE;
    double PosZAnteriorBloque = Double.MAX_VALUE;
    int slowFallingDuration = 1000;

    MobEffectInstance slowFallingEffect = new MobEffectInstance(MobEffects.SLOW_FALLING, slowFallingDuration, 500);


    public ExampleMod(){
        super();
    }

    @Override
    public String autor() {
        return "Aitor Zubillaga Soria";
    }


    /*Hace que los metodos de eventos no funcionen hasta que se inicia el mundo para evitar errores*/
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Activa la bandera cuando se activa el evento ServerStartingEvent
        serverStarting = true;
    }



    /*Cada vez que te mueves, actualiza las vaiables de posición*/
    @SubscribeEvent
    public void ActualizadorPosiciones( MovementInputUpdateEvent movement) {

        if (!serverStarting) {
            return; // El evento ServerStartingEvent no se ha activado, saliendo del método
        }


        BlockPos posicion1 = new BlockPos(movement.getEntity().xOld, movement.getEntity().yOld, movement.getEntity().zOld);
        BlockPos posicion2 = new BlockPos(movement.getEntity().xOld, movement.getEntity().yOld-1, movement.getEntity().zOld);
        //System.out.println(posicion.toString());
        if(movement.getEntity() instanceof Player){
            if(movement.getInput().down){
                this.posicionJugador = posicion1;
                this.posicionBloqueDebajo = posicion2;


            }
            if(movement.getInput().up){
                this.posicionJugador = posicion1;
                this.posicionBloqueDebajo = posicion2;

            }
            if(movement.getInput().right){
                this.posicionJugador = posicion1;
                this.posicionBloqueDebajo = posicion2;

            }
            if(movement.getInput().left){
                this.posicionJugador = posicion1;
                this.posicionBloqueDebajo = posicion2;

            }
        }

    }





/*Detecta que botas llevas y llama a sus correspondientes metodos*/
    @SubscribeEvent
    public void selectorDeBotas(MovementInputUpdateEvent movement) {
        if (!serverStarting) {
            return; // El evento ServerStartingEvent no se ha activado, saliendo del método
        }

        //Instancio las variables de el jugador y el mundo
        jugador = movement.getEntity();
        mundo = jugador.getCommandSenderWorld();

        ItemStack botas = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.FEET);

        if (botas.getItem().equals( Items.DIAMOND_BOOTS)) {
            //System.out.println("DIAMOND_BOOTS");
            botasDiamante();

        }
        else if (botas.getItem().equals( Items.GOLDEN_BOOTS)) {
            //System.out.println("GOLDEN_BOOTS");

            botasOro();
        }
       else  if (botas.getItem().equals( Items.IRON_BOOTS)) {
            //System.out.println("IRON_BOOTS");
            botasHierro();
        }
        else  if (botas.getItem().equals( Items.LEATHER_BOOTS)) {
            //System.out.println("LEATHER_BOOTS");
            botasCuero();

        } else if (botas.getItem().equals( Items.NETHERITE_BOOTS)) {
            //System.out.println("NETHERITE_BOOTS");
            botasNetherita();


        }else if(botas.getItem().equals( Items.CHAINMAIL_BOOTS)){
            botasCotaDeMalla();
        }else{
            //System.out.println("NO LLEVAS NADA");
        }



    }




    /*Aqui Ocurre el evento de las botas de cuero*/
    public void botasCuero() {

        if(cambioDeBloque()){

            if (estaTocandoSuelo()) {
            if (Math.random() < 0.5) {
                //comprueba si hay bloque debajo
                colocarBloqueServer(Blocks.FIRE, posicioAnteriorJugador);
                System.out.println("Fuego Invocado");
            }else{ colocarBloqueServer(Blocks.SOUL_FIRE, posicioAnteriorJugador);//he variado porque me gusta mas así
                System.out.println("Nada");}
                } else {}
        }
    }





/*Aqui Ocurre el evento de las botas de hierro*/
    private void botasHierro() {
        if(cambioDeBloque()){
            if (Math.random() < 0.10) {
                generarExplosion();
                System.out.println("Explosion");
            }else{
                System.out.println("Nada");
            }
        }
    }


/*Ocurre el evento de las botas de oro*/
        private void botasOro() {
            if(cambioDeBloque()){

                if(estaTocandoSuelo()){
                    //System.out.println("Estas pisando suelo");
                    colocarBloqueServer(Blocks.GOLD_BLOCK,posicionBloqueDebajo);
                }else{
                    //System.out.println("Estas Flotando");
                }

            }
        }
    /*Ocurre el evento de las botas de diamante*/
    private void botasDiamante() {
        if(cambioDeBloque()){
            int idFlor = (int) (Math.random() * 15) + 1666;
            Level level = Minecraft.getInstance().level;
            BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL),Minecraft.getInstance().player.getCommandSenderWorld(), posicionBloqueDebajo,Minecraft.getInstance().player);
            colocarBloqueServerPorID(idFlor,posicionJugador);
        }
    }


    /*Ocurre el evento de las botas de netherite*/
    public void botasNetherita() {
     if( Minecraft.getInstance().level.getBlockState(posicionBloqueDebajo).getBlock()== Blocks.LAVA|| Minecraft.getInstance().level.getBlockState(posicionJugador).getBlock()== Blocks.LAVA){
         System.out.println("Debajo tienes lava");
         Minecraft.getInstance().player.setNoGravity(true);

     }else{
         Minecraft.getInstance().player.setNoGravity(false);
     }
    }

    /*Ocurre el evento de las botas de cotaDeMalla*/
    public void botasCotaDeMalla() {
        Double aceleracion = -1.5;
        //si el jugador sobrepasa una aceleracion, da el efecto de caida lenta
        if (jugador.getDeltaMovement().y < aceleracion) {
            jugador.setDeltaMovement(jugador.getDeltaMovement().x,0,jugador.getDeltaMovement().z);
            jugador.addEffect(slowFallingEffect);
        // Disminuye el temporizador en cada actualización de la entidad del jugador
        if (slowFallingDuration > 0) {
            slowFallingDuration--;}
            // Cuando el temporizador llega a 0, quita el efecto de la poción
            if (slowFallingDuration == 0) {
                jugador.removeAllEffects();
                jugador.removeEffectNoUpdate(slowFallingEffect.getEffect());
                //System.out.println("REMOVE EFFECT");
            }

    }}
    /*Crea una explosion en la posición del jugador de un poder determinado*/
    public void generarExplosion(){
        float potencia = 2.0f;
        Explosion explosion = new Explosion(Minecraft.getInstance().level, null, posicionBloqueDebajo.getX(), posicionBloqueDebajo.getY(),posicionBloqueDebajo.getZ(),potencia, true, Explosion.BlockInteraction.BREAK);
        explosion.explode();
        explosion.finalizeExplosion(true);
        explosion.getExploder();
        explosion.getSourceMob();
    }




    /*Este metodo si lo llamas, comprueba desde la ultima vez que lo has llamado y devueve true si has cambiado de bloque*/

  public boolean cambioDeBloque(){
        if(((int)posicionJugador.getX())!=((int)PosXAnteriorJugador)&&PosXAnteriorJugador!=Double.MAX_VALUE){
            //System.out.println("Has cambiado de bloque X");
            this.PosXAnteriorJugador = posicionJugador.getX();
            this.PosZAnteriorJugador = posicionJugador.getZ();

            return true;
        } else if (((int)posicionJugador.getZ())!=((int)PosZAnteriorJugador)&&PosZAnteriorJugador!=Double.MAX_VALUE){
          //  System.out.println("Has cambiado de bloque Z");
            this.PosXAnteriorJugador = posicionJugador.getX();
            this.PosZAnteriorJugador = posicionJugador.getZ();

            return true;
        }else{
            this.PosXAnteriorJugador = posicionJugador.getX();
            this.PosZAnteriorJugador = posicionJugador.getZ();
            posicioAnteriorJugador = new BlockPos(PosXAnteriorJugador,posicionJugador.getY(),PosZAnteriorJugador);
           // System.out.println(posicioAnteriorJugador.toString());

        }

        return false;
    }

        public boolean estaTocandoSuelo(){
            BlockState bloqueDebajo = Minecraft.getInstance().level.getBlockState(posicionBloqueDebajo);
            return bloqueDebajo.getBlock() != Blocks.AIR;
        }
    public boolean estaEnAire(){
        BlockState bloqueDebajo = Minecraft.getInstance().level.getBlockState(posicionJugador);
        return bloqueDebajo.getBlock().equals(Blocks.AIR) ;
    }


    public void colocarBloqueServer(Block blockNombre,BlockPos coordenadas ) {
        // System.out.println("Se ejecuto:colocarBloque");
       // Minecraft.getInstance().level.setBlock(coordenadas, Block.stateById(blockId), 512);
        BlockState bloque = blockNombre.defaultBlockState();
      mundo.setBlockAndUpdate(coordenadas,bloque);
        //movement.getEntity().getLevel().setBlock(coordenadas, Block.stateById(blockId), 512);
    }

    public void colocarBloqueServerPorID(int blockId,BlockPos coordenadas ) {
         Minecraft.getInstance().level.setBlock(coordenadas, Block.stateById(blockId), 512);

    }




















    @Override
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        System.out.println("Bloque destruido en la posicion "+pos);
    }

    @Override
    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
        LOGGER.info("Server starting");
    }

    @Override
    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        LOGGER.info("Item recogido");
        System.out.println("Item recogido");
    }

    @Override
    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        System.out.println("evento LivingDamageEvent invocado "+event.getEntity().getClass()+" provocado por "+event.getSource().getEntity());
    }

    @Override
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        System.out.println("evento LivingDeathEvent invocado "+event.getEntity().getClass()+" provocado por "+event.getSource().getEntity());

    }

    @Override
    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        LOGGER.info("evento LivingEntityUseItemEvent invocado "+event.getEntity().getClass());
    }


    @Override
    @SubscribeEvent
    public void onPlayerFish(ItemFishedEvent event) {
        System.out.println("¡Has pescado un pez!");
    }

    @SubscribeEvent
    public void onPlayerTouch(PlayerInteractEvent.RightClickBlock event) {
        System.out.println("¡Has hecho click derecho!");
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        if (ItemStack.EMPTY.equals(heldItem)) {
            System.out.println("La mano esta vacia");
            if (state.getBlock().getName().getString().trim().toLowerCase().endsWith("log")) {
                System.out.println("¡Has hecho click sobre un tronco!");
            }
        }
    }
   @Override

    @SubscribeEvent
    public void onPlayerWalk(MovementInputUpdateEvent event) {
        if(event.getEntity() instanceof Player){
            if(event.getInput().down){
             //   System.out.println("down"+event.getInput().down);
            }
            if(event.getInput().up){
              //  System.out.println("up"+event.getInput().up);
            }
            if(event.getInput().right){
             //   System.out.println("right"+event.getInput().right);
            }
            if(event.getInput().left){
              //  System.out.println("left"+event.getInput().left);
            }
        }
    }
}
