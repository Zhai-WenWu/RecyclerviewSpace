    SpaceItemDecoration decoration = new SpaceItemDecoration.Builder()
                .setLeftEdge(ResUtil.getDimen(R.dimen.home_item_padding_left))
                .setRightEdge(ResUtil.getDimen(R.dimen.home_item_padding_left))
                .setHSpace(GMViewUtil.dip2px(mContext, 15))
                .build();
        rv_goods.addItemDecoration(decoration);

