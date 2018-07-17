<template>
    <div id="app">
        <field></field>
    </div>
</template>

<script>

    import Field from '@/components/field'

    export default {

        name: 'app',
        components: {
            Field
        }
    }
</script>

<style lang="scss">
    #app {
        display: flex;
        justify-content: center;
    }
</style>
