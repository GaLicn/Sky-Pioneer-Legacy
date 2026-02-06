StartupEvents.registry('item', event => {
    event.create('wood_brick')
        .food(food => {
            food.nutrition(0)
            food.saturation(0)
            food.alwaysEdible(true)
        })
    
    event.create('wood_residue')
})

