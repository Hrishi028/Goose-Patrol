package com.goosepatrol.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

public class DecorationRenderSystem implements GameSystem {

    private final SpriteBatch batch = new SpriteBatch();
    private final Texture sheet;

    private final TextureRegion tree1;
    private final TextureRegion tree2;
    private final TextureRegion tree3;
    private final TextureRegion tree4;

    private final TextureRegion bush1;
    private final TextureRegion bush2;
    private final TextureRegion bush3;
    private final TextureRegion bush4;

    private final TextureRegion flowers1;
    private final TextureRegion flowers2;
    private final TextureRegion flowers3;
    private final TextureRegion flowers4;

    private final TextureRegion bench;
    private final TextureRegion lamp;
    private final TextureRegion flowerBed;

    public DecorationRenderSystem() {

        sheet = new Texture(
            Gdx.files.internal(
                "map/decorations/campus_decorations.png"
            )
        );

        sheet.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        tree1 = crop(24, 36, 338, 358);
        tree2 = crop(394, 18, 382, 376);
        tree3 = crop(793, 34, 364, 363);
        tree4 = crop(1180, 35, 341, 363);

        bush1 = crop(46, 443, 249, 154);
        bush2 = crop(361, 444, 241, 153);
        bush3 = crop(653, 447, 253, 146);
        bush4 = crop(962, 451, 221, 141);

        flowers1 = crop(33, 639, 276, 127);
        flowers2 = crop(356, 639, 235, 113);
        flowers3 = crop(640, 627, 255, 136);
        flowers4 = crop(943, 634, 250, 125);

        bench = crop(56, 845, 295, 149);
        lamp = crop(449, 764, 76, 239);
        flowerBed = crop(586, 835, 319, 141);
    }

    private TextureRegion crop(
        int x, int y, int width, int height
    ) {
        return new TextureRegion(sheet, x, y, width, height);
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        float s = Math.min(w / 1300f, h / 1000f);

        batch.begin();

        // =====================================
        // UPPER LEFT GARDEN
        // =====================================

        batch.draw(tree1,
            w * 0.28f, h * 0.77f,
            115 * s, 125 * s);

        batch.draw(tree2,
            w * 0.39f, h * 0.80f,
            120 * s, 130 * s);

        batch.draw(bush1,
            w * 0.08f, h * 0.54f,
            65 * s, 40 * s);

        batch.draw(flowers1,
            w * 0.28f, h * 0.55f,
            75 * s, 35 * s);

        // =====================================
        // UPPER MIDDLE GARDEN
        // Keep the top-right building clear.
        // =====================================

        batch.draw(bush2,
            w * 0.43f, h * 0.55f,
            65 * s, 40 * s);

        batch.draw(flowers2,
            w * 0.50f, h * 0.53f,
            65 * s, 32 * s);

        // =====================================
        // LOWER LEFT GARDEN
        // =====================================

        batch.draw(tree2,
            w * 0.32f, h * 0.09f,
            110 * s, 120 * s);

        batch.draw(tree3,
            w * 0.63f, h * 0.09f,
            105 * s, 115 * s);

        batch.draw(bush3,
            w * 0.08f, h * 0.34f,
            65 * s, 40 * s);

        batch.draw(flowers3,
            w * 0.29f, h * 0.34f,
            75 * s, 35 * s);

        // =====================================
        // LOWER MIDDLE GARDEN
        // Keep the lower-right building clear.
        // =====================================

        batch.draw(tree1,
            w * 0.43f, h * 0.10f,
            100 * s, 110 * s);

        batch.draw(bush4,
            w * 0.68f, h * 0.34f,
            65 * s, 40 * s);

        batch.draw(flowers4,
            w * 0.77f, h * 0.34f,
            75 * s, 35 * s);

        // =====================================
        // BENCHES
        // =====================================

        batch.draw(bench,
            w * 0.16f, h * 0.58f,
            80 * s, 40 * s);

        batch.draw(bench,
            w * 0.72f, h * 0.58f,
            80 * s, 40 * s);

        batch.draw(bench,
            w * 0.16f, h * 0.30f,
            80 * s, 40 * s);

        batch.draw(bench,
            w * 0.72f, h * 0.30f,
            80 * s, 40 * s);

        // =====================================
        // LAMPS
        // =====================================

        batch.draw(lamp,
            w * 0.37f, h * 0.54f,
            20 * s, 62 * s);

        batch.draw(lamp,
            w * 0.64f, h * 0.54f,
            20 * s, 62 * s);

        batch.draw(lamp,
            w * 0.37f, h * 0.30f,
            20 * s, 62 * s);

        batch.draw(lamp,
            w * 0.61f, h * 0.30f,
            20 * s, 62 * s);

        // =====================================
        // FLOWER BEDS
        // =====================================

        batch.draw(flowerBed,
            w * 0.08f, h * 0.25f,
            75 * s, 34 * s);

        batch.draw(flowerBed,
            w * 0.83f, h * 0.25f,
            75 * s, 34 * s);

        batch.end();
    }

    public void dispose() {
        batch.dispose();
        sheet.dispose();
    }
}
