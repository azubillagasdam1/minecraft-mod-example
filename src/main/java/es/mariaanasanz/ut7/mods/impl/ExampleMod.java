package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
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
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
    private   int contador = 1650;

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
            direccionPosicionAnteriorJugador();
            botasCuero();

        } else if (botas.getItem().equals( Items.NETHERITE_BOOTS)) {
            //System.out.println("NETHERITE_BOOTS");


        }else{
            //System.out.println("NO LLEVAS NADA");
        }



    }




    /*Aqui Ocurre el evento de las botas de cuero*/
    public void botasCuero() {

        if(cambioDeBloque()){
            if (Math.random() < 0.5) {
                //comprueba si hay bloque debajo
                BlockState bloqueDebajo = Minecraft.getInstance().level.getBlockState(posicionBloqueDebajo);
                if (bloqueDebajo.getBlock() != Blocks.AIR) {
                    colocarBloqueServer(1696, bloqueDeDetras());
                    System.out.println("Fuego Invocado");
                } else {
                    System.out.println("Nada");
                }
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



        private void botasOro() {
            if(cambioDeBloque()){
                BlockState bloqueDebajo = Minecraft.getInstance().level.getBlockState(posicionBloqueDebajo);
                if(bloqueDebajo.getBlock()!= Blocks.AIR){
                    //System.out.println("Estas pisando suelo");
                    colocarBloqueServer(1681,posicionBloqueDebajo);
                }else{
                    //System.out.println("Estas Flotando");
                }

            }
        }

    private void botasDiamante() {
        if(cambioDeBloque()){
            int idFlor = (int) (Math.random() * 15) + 1666;
            Level level = Minecraft.getInstance().level;
            BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL),Minecraft.getInstance().player.getCommandSenderWorld(), posicionBloqueDebajo,Minecraft.getInstance().player);
            colocarBloqueServer(idFlor,posicionJugador);
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



    public String direccionPosicionAnteriorJugador() {
        double nuevaX = posicionJugador.getX();
        double nuevaZ = posicionJugador.getZ();
        String direccion = "";

        if (nuevaZ > PosZAnteriorJugador) {
            direccion = "SUR";
        } else if (nuevaZ < PosZAnteriorJugador) {
            direccion = "NORTE";
        } else if (nuevaX > PosXAnteriorJugador) {
            direccion = "ESTE";
        } else if (nuevaX < PosXAnteriorJugador) {
            direccion = "OESTE";
        }

        // Actualizar las coordenadas anteriores con las nuevas coordenadas del jugador
        PosXAnteriorJugador = nuevaX;
        PosZAnteriorJugador = nuevaZ;
        return  direccion;
    }


    public BlockPos bloqueDeDetras() {
        double x = posicionJugador.getX();
        double y = posicionJugador.getY();
        double z = posicionJugador.getZ();
        switch (direccionPosicionAnteriorJugador()){
            case "SUR" : return new BlockPos(x,y,z-1);

            case "NORTE" :return new BlockPos(x,y,z+1);

            case "ESTE" :return new BlockPos(x-1,y,z);

            case "OESTE" :return new BlockPos(x+1,y,z);

            default:
                System.out.println("ERROORRR");
                return new BlockPos(x,y,z);
        }
    }








    /*Coloca un bloque que recibe como parametro*/
  /* public void colocarBloque(int blockId, MovementInputUpdateEvent movement) {
       // System.out.println("Se ejecuto:colocarBloque");
        BlockPos coordenadas = getPosicionJugador();
        //movement.getEntity().getLevel().setBlock(coordenadas, Block.stateById(blockId), 512);
        Minecraft.getInstance().level.setBlock(coordenadas, Block.stateById(blockId), 512);
    }*/


    public void colocarBloqueServer(int blockId,BlockPos coordenadas ) {
        // System.out.println("Se ejecuto:colocarBloque");
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
