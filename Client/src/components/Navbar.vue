<template>
    <b-navbar type="is-light" style="margin: 10px">
        <template #brand>
            <b-navbar-item tag="router-link" :to="{ path: '/' }">
                <b>
                    Deepsea
                </b>
            </b-navbar-item>
        </template>

        <template #start>
            <b-navbar-item href="/projects">
                Projects
            </b-navbar-item>
            <b-navbar-item href="/teams">
                Teams
            </b-navbar-item>
        </template>

        <template #end>
            <b-navbar-item tag="div">
                <div class="buttons">
                    <router-link to="/profile">
                        <a class="button is-light">
                            Profile
                        </a>
                    </router-link>

                    <a class="button is-success" @click="logout">
                        <strong>Logout</strong>
                    </a>
                </div>
            </b-navbar-item>
        </template>
    </b-navbar>
</template>
<script>
export default {
    name: "Navbar",

    methods:{
        logout: function(){
            

            axios.post('/auth/users/logout', {
                
            }).then((response) => {
                sessions.storage.removeItem('auth-token');
                sessions.storage.removeItem('auth-user');
                this.$router.push('/login');
            }).catch((error) => {
                

            });
        }
    }

};
</script>

<style>
.logo {
    width: 120px;
    height: 31px;
    background: rgba(255, 255, 255, 0.2);
    margin: 16px 28px 16px 0;
    float: left;
}
</style>
