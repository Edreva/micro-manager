package org.micromanager.plugins.DisplayIlluminator;

import java.util.Objects;

public class Utilities {
    public static String colorToHexString(java.awt.Color color) {
        return String.format("%06x", color.getRGB() & 0xFFFFFF);
    }

    public static ImageMode getImageModeFromImageName(String imageName) {
        return ImageMode.valueOf(imageName.replaceAll("\\d", "").toUpperCase());
    }

    public enum ImageMode {
        DPC("DPC"),
        BF("BF"),
        DF("DF"),
        PC("PC"),
        RB("RB"),
        OFF("Off");

        private final String modeName;

        ImageMode(String modeName) {
            this.modeName = modeName;
        }

        @Override
        public String toString() {
            return modeName;
        }
    }

    public enum DevicePropertyName {
        DISPLAY_HEIGHT("DisplayHeight_pixels"),
        DISPLAY_WIDTH("DisplayWidth_pixels"),
        CENTER_X("CenterX"),
        CENTER_Y("CenterY"),
        ROTATION("Rotation"),
        COLOR("MonoColor"),
        ACTIVE_IMAGE("ActiveImage"),
        BF_HEIGHT("BfHeight"),
        BF_WIDTH("BfWidth"),
        DF_HEIGHT("DfHeight"),
        DF_WIDTH("DfWidth"),
        DF_INNER_HEIGHT("DfInnerHeight"),
        DF_INNER_WIDTH("DfInnerWidth"),
        DPC_COUNT("DpcPatternCount"),
        DPC_HEIGHT("DpcHeight"),
        DPC_WIDTH("DpcWidth"),
        DPC_INNER_HEIGHT("DpcInnerHeight"),
        DPC_INNER_WIDTH("DpcInnerWidth"),
        PC_HEIGHT("PcHeight"),
        PC_WIDTH("PcWidth"),
        PC_INNER_HEIGHT("PcInnerHeight"),
        PC_INNER_WIDTH("PcInnerWidth"),
        RB_OUTER_COLOR("RbOuterColor"),
        RB_INNER_COLOR("RbInnerColor");

        private final String propertyName;

        DevicePropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getName() {
            return propertyName;
        }

        @Override
        public String toString() {
            return propertyName;
        }

    }

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {

        /**
         * Performs this operation on the given arguments.
         *
         * @param t the first input argument
         * @param u the second input argument
         * @param v the third input argument
         */
        void accept(T t, U u, V v);

        /**
         * Returns a composed {@code TriConsumer} that performs, in sequence, this
         * operation followed by the {@code after} operation. If performing either
         * operation throws an exception, it is relayed to the caller of the
         * composed operation. If performing this operation throws an exception,
         * the {@code after} operation will not be performed.
         *
         * @param after the operation to perform after this operation
         * @return a composed {@code TriConsumer} that performs in sequence this
         * operation followed by the {@code after} operation
         * @throws NullPointerException if {@code after} is null
         */
        default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
            Objects.requireNonNull(after);
            return (t, u, v) -> {
                accept(t, u, v);
                after.accept(t, u, v);
            };
        }
    }
}
