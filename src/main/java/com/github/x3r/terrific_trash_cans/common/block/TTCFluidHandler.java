package com.github.x3r.terrific_trash_cans.common.block;

import com.github.x3r.terrific_trash_cans.TerrificTrashCans;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Predicate;

public class TTCFluidHandler implements IFluidHandler, INBTSerializable<CompoundTag> {

    public static final String TAG_KEY = "FluidHandler";
    private final FluidTank[] tanks;
    public TTCFluidHandler(FluidTank[] tanks) {
        this.tanks = tanks;
    }

    @Override
    public int getTanks() {
        return tanks.length;
    }

    public FluidTank getTank(int tank) {
        FluidTank fluidTank = null;
        try {
            fluidTank = tanks[tank];
        } catch (ArrayIndexOutOfBoundsException exception) {
            TerrificTrashCans.LOGGER.warn("Attempted to access invalid tank");
        }
        return fluidTank;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return getTank(tank).getFluid();
    }

    public void setFluidInTank(int tank, FluidStack stack) {
        getTank(tank).setFluid(stack);
    }

    @Override
    public int getTankCapacity(int tank) {
        return getTank(tank).getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return getTank(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        FluidStack copy = resource.copy();
        for (int i = 0; i < tanks.length; i++) {
            copy.shrink(tanks[i].fill(copy, action));
        }
        return resource.getAmount() - copy.getAmount();
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack copy = resource.copy();
        for (int i = tanks.length - 1; i >= 0; i--) {
            copy.shrink(tanks[i].drain(copy, action).getAmount());
        }
        return new FluidStack(resource.getFluid(), resource.getAmount() - copy.getAmount());
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        int drained = 0;
        Fluid fluid = Arrays.stream(tanks).map(t -> t.fluid.getFluid()).filter(f -> f != Fluids.EMPTY).toList().get(getTanks() - 1);
        for (int i = tanks.length - 1; i >= 0; i--) {
            if(tanks[i].getFluid().getFluid().isSame(fluid)) {
                FluidStack stack = tanks[i].drain(maxDrain - drained, action);
                drained += stack.getAmount();
            }
        }
        return new FluidStack(fluid, drained);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag listTag = new ListTag();
        for (int i = 0; i < getTanks(); i++) {
            CompoundTag compoundTag = new CompoundTag();
            getFluidInTank(i).writeToNBT(compoundTag);
            compoundTag.putInt("Capacity", getTankCapacity(i));

            listTag.add(compoundTag);
        }
        nbt.put("IFluidHandler", listTag);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag listTag = nbt.getCompound(TTCFluidHandler.TAG_KEY).getList("IFluidHandler", 10);
        FluidTank[] fluidTanks = listTag.stream().map(compoundTag -> {
            FluidStack stack = FluidStack.loadFluidStackFromNBT((CompoundTag) compoundTag);
            int capacity = ((CompoundTag) compoundTag).getInt("Capacity");
            FluidTank tank = new FluidTank(capacity);
            tank.setFluid(stack);

            return tank;
        }).toArray(FluidTank[]::new);
        for (int i = 0; i < getTanks(); i++) {
            if(getTankCapacity(i) != fluidTanks[i].getCapacity()) {
                TerrificTrashCans.LOGGER.warn("Tank capacity was saved incorrectly or changed, still attempting to fill tank");
            }
            setFluidInTank(i, fluidTanks[i].getFluid());
        }
    }

    public static class FluidTank implements IFluidTank {

        private final int capacity;
        private final Predicate<FluidStack> validator;
        private FluidStack fluid = FluidStack.EMPTY;

        public FluidTank(int capacity) {
            this(capacity, fluidStack -> true);
        }

        public FluidTank(int capacity, Predicate<FluidStack> validator) {
            this.capacity = capacity;
            this.validator = validator;
        }

        @Override
        public @NotNull FluidStack getFluid() {
            return this.fluid;
        }

        @Override
        public int getFluidAmount() {
            return this.fluid.getAmount();
        }

        @Override
        public int getCapacity() {
            return this.capacity;
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return validator.test(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || !isFluidValid(resource)) {
                return 0;
            }
            if (action.simulate()) {
                if (fluid.isEmpty()) {
                    return Math.min(capacity, resource.getAmount());
                }
                if (!fluid.isFluidEqual(resource)) {
                    return 0;
                }
                return Math.min(capacity - fluid.getAmount(), resource.getAmount());
            }
            if (fluid.isEmpty()) {
                fluid = new FluidStack(resource, Math.min(capacity, resource.getAmount()));
                onContentsChanged();
                return fluid.getAmount();
            }
            if (!fluid.isFluidEqual(resource)) {
                return 0;
            }
            int filled = capacity - fluid.getAmount();

            if (resource.getAmount() < filled) {
                fluid.grow(resource.getAmount());
                filled = resource.getAmount();
            }
            else {
                fluid.setAmount(capacity);
            }
            if (filled > 0) onContentsChanged();
            return filled;
        }

        //Just in case its needed later
        protected void onContentsChanged() {

        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || !resource.isFluidEqual(fluid)) {
                return FluidStack.EMPTY;
            }
            return drain(resource.getAmount(), action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            int drained = maxDrain;
            if (fluid.getAmount() < drained) {
                drained = fluid.getAmount();
            }
            FluidStack stack = new FluidStack(fluid, drained);
            if (action.execute() && drained > 0) {
                fluid.shrink(drained);
                onContentsChanged();
            }
            return stack;
        }

        public void setFluid(FluidStack stack) {
            this.fluid = stack;
        }

    }
}
