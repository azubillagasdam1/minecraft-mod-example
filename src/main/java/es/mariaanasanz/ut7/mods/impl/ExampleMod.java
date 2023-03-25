package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(DamMod.MOD_ID)
public class ExampleMod extends DamMod implements IBlockBreakEvent, IServerStartEvent,
        IItemPickupEvent, ILivingDamageEvent, IUseItemEvent, IFishedEvent,
        IInteractEvent, IMovementEvent {


    //   private boolean yaLlamado = false;//usada para controlar los bucles infinitos que se crean al llamar a "MovementInputUpdateEvent movement"
    private BlockPos posicionJugador;
    private BlockPos posicionBloqueDebajo;
    double PosXAnteriorJugador = Double.MAX_VALUE;
    double PosZAnteriorJugador = Double.MAX_VALUE;
    double PosXAnteriorBloque = Double.MAX_VALUE;
    double PosZAnteriorBloque = Double.MAX_VALUE;
    private   int contador = 0;

    public ExampleMod(){
        super();
    }

    @Override
    public String autor() {
        return "Aitor Zubillaga Soria";
    }

    /* Devuelve 1 o -1  dependiendo de la direccion que te muevas*/

    /*@SubscribeEvent
    public void vectores(MovementInputUpdateEvent movement) {
            if(movement.getInput().getMoveVector().x==1.0){
                System.out.println("Vector X = "+(int)movement.getInput().getMoveVector().x);
            }
        if(movement.getInput().getMoveVector().x==-1.0){
            System.out.println("Vector X = "+(int)movement.getInput().getMoveVector().x);
        }
        if(movement.getInput().getMoveVector().y==1.0){
            System.out.println("Vector Y = "+(int)movement.getInput().getMoveVector().y);
        }
        if(movement.getInput().getMoveVector().y==-1.0){
            System.out.println("Vector Y = "+(int)movement.getInput().getMoveVector().y);
        }
        //System.out.println("Vector en tiempo real: "+movement.getInput().getMoveVector().x +" "+ movement.getInput().getMoveVector().y);
    }*/

    /*Devuelve las cordenadas en directo de el bloque */
   /* @SubscribeEvent
    public void printCordenadasJugador(MovementInputUpdateEvent movement) {
        System.out.print("X: "+(int)movement.getEntity().xOld+"\t");
        System.out.print("Y: "+(int)movement.getEntity().yOld+"\t");
        System.out.print("Z: "+(int)movement.getEntity().yOld+"\t");

    }

    @SubscribeEvent
    public void printCordenadasBolqueDebajo(MovementInputUpdateEvent movement) {
        System.out.print("X: "+(int)movement.getEntity().xOld+"\t");
        System.out.print("Y: "+(int)(movement.getEntity().yOld-1)+"\t");
        System.out.print("Z: "+(int)movement.getEntity().zOld+"\t");

    }*/



    /*Devuelve el BlockPos de del jugador*/
    @SubscribeEvent
    public void PosicionJugador( MovementInputUpdateEvent movement) {

        BlockPos posicion = new BlockPos(movement.getEntity().xOld, movement.getEntity().yOld, movement.getEntity().zOld);
        //System.out.println(posicion.toString());
        this.posicionJugador = posicion;
    }
    @SubscribeEvent
    public void PosicionBloqueDebajo( MovementInputUpdateEvent movement) {

        BlockPos posicion = new BlockPos(movement.getEntity().xOld, movement.getEntity().yOld-1, movement.getEntity().zOld);
        //System.out.println(posicion.toString());

        this.posicionBloqueDebajo = posicion;
    }





/*
 @SubscribeEvent
    public BlockPos getPosicionJugador() {
            Player player = Minecraft.getInstance().player;
        BlockPos playerPos = new BlockPos(player.getOnPos().getX(), player.getOnPos().getY(), player.getOnPos().getX());
            System.out.println("La posición actual del jugador es: " + playerPos);
    return  playerPos;}
 */
    /**/
    /*@SubscribeEvent
    public void reemplazarBloque(MovementInputUpdateEvent movement) {
       // yaLlamado = false;
        selectorDeBotas(movement);
       // System.out.println("Se ejecutó el método reemplazarBloque");
    }*/


    /*Comprueba si el jugador lleva botas, y que tipo de botas para llamar a sus respectivos metodos*/
    @SubscribeEvent
    public void selectorDeBotas(MovementInputUpdateEvent movement) {
        ItemStack botas = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.FEET);

        if (botas.getItem().equals( Items.DIAMOND_BOOTS)) {
            System.out.println("DIAMOND_BOOTS");
        }
        else if (botas.getItem().equals( Items.GOLDEN_BOOTS)) {
            System.out.println("GOLDEN_BOOTS");
        }
       else  if (botas.getItem().equals( Items.IRON_BOOTS)) {
            //System.out.println("IRON_BOOTS");
            botasHierro();
        }
        else  if (botas.getItem().equals( Items.LEATHER_BOOTS)) {
            //System.out.println("LEATHER_BOOTS");
            botasCuero();
        }else{
            System.out.println("NO LLEVAS NADA");
        }



    }



    /*Aqui Ocurre el evento de las botas de cuero*/
    public void botasCuero() {

        if(cambioDeBloque()){
            if (Math.random() < 0.5) {
                colocarBloqueServer(1696);
                System.out.println("Fuego Invocado");
            }else{
               System.out.println("Nada");
            }
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

    public void generarExplosion(){
        Explosion explosion = new Explosion(Minecraft.getInstance().level, null, posicionBloqueDebajo.getX(), posicionBloqueDebajo.getY(),posicionBloqueDebajo.getZ(),2.0f, true, Explosion.BlockInteraction.BREAK);
        explosion.explode();
        explosion.finalizeExplosion(true);
        explosion.getExploder();
        explosion.getSourceMob();
    }




    /*Este metodo si lo llamas, comprueba desde la ultima vez que lo has llamado y devueve true si has cambiado de bloque*/


    public boolean cambioDeBloque(){
        if(((int)posicionJugador.getX())!=((int)PosXAnteriorJugador)&&PosXAnteriorJugador!=Double.MAX_VALUE){
            System.out.println("Has cambiado de bloque X");
            this.PosXAnteriorJugador = posicionJugador.getX();
            this.PosZAnteriorJugador = posicionJugador.getZ();

            return true;
        } else if (((int)posicionJugador.getZ())!=((int)PosZAnteriorJugador)&&PosZAnteriorJugador!=Double.MAX_VALUE){
            System.out.println("Has cambiado de bloque Z");
            this.PosXAnteriorJugador = posicionJugador.getX();
            this.PosZAnteriorJugador = posicionJugador.getZ();

            return true;
        }else{
            this.PosXAnteriorJugador = posicionJugador.getX();
            this.PosZAnteriorJugador = posicionJugador.getZ();

        }

        return false;
    }



   /* public int direccionPosicionAnteriorJugador(){
        if(cambioDeBloque()){
             if((int)PosXAnteriorJugador - (int)(posicionJugador.getX())<0){
            System.out.println("Anterior-actualX<0 ID:1");
            return 1;
           }
            if((int)PosXAnteriorJugador - (int)(posicionJugador.getX())>0){
                System.out.println("Anterior-actualX>0 ID:2");
                return 2;
            }
            if((int)PosZAnteriorJugador - (int)(posicionJugador.getZ())<0){
                System.out.println("Anterior-actualZ>0 ID:3");
                return 3;
            }
            if((int)PosZAnteriorJugador - (int)(posicionJugador.getZ())>0){
                System.out.println("Anterior-actualZ>0 ID:4");
                return 4;
            }


            }else{
            System.out.println("Anterior-actualZ==0 ID:0");

        }
        return 0;

    }*/





    /*Coloca un bloque que recibe como parametro*/
  /* public void colocarBloque(int blockId, MovementInputUpdateEvent movement) {
       // System.out.println("Se ejecuto:colocarBloque");
        BlockPos coordenadas = getPosicionJugador();
        //movement.getEntity().getLevel().setBlock(coordenadas, Block.stateById(blockId), 512);
        Minecraft.getInstance().level.setBlock(coordenadas, Block.stateById(blockId), 512);
    }*/


    public void colocarBloqueServer(int blockId) {
        // System.out.println("Se ejecuto:colocarBloque");
        BlockPos coordenadas = posicionBloqueDebajo;
        Minecraft.getInstance().level.setBlock(coordenadas, Block.stateById(blockId), 512);
        //movement.getEntity().getLevel().setBlock(coordenadas, Block.stateById(blockId), 512);
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

    @Override
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
                System.out.println("down"+event.getInput().down);
            }
            if(event.getInput().up){
                System.out.println("up"+event.getInput().up);
            }
            if(event.getInput().right){
                System.out.println("right"+event.getInput().right);
            }
            if(event.getInput().left){
                System.out.println("left"+event.getInput().left);
            }
        }
    }
}
