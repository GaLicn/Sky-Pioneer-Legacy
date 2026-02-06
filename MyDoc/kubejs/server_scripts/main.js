ItemEvents.foodEaten(event => {
    if (event.item.id === 'kubejs:wood_brick') {
        event.player.give('kubejs:wood_residue')
    }
})
