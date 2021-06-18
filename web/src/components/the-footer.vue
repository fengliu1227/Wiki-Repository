<template>
    <a-layout-footer style="text-align: center">
        Andrew's wiki repository
    </a-layout-footer>
</template>

<script lang="ts">
    import { defineComponent, computed, onMounted } from 'vue';
    import store from "@/store";
    import {Tool} from "@/util/tool";
    import { notification } from 'ant-design-vue';

    export default defineComponent({
        name: 'the-footer',
        setup() {
            const user = computed(() => store.state.user);

            let websocket: any;
            let token: any;
            const onOpen = () => {
                console.log('WebSocket Connection Successfully，State Code：', websocket.readyState)
            };
            const onMessage = (event: any) => {
                notification['info']({
                    message: 'Message Received',
                    description: event.data,
                });
            };
            const onError = () => {
                console.log('WebSocket Connection failed，State Code：', websocket.readyState)
            };
            const onClose = () => {
                console.log('WebSocket disconnection，State Code：', websocket.readyState)
            };
            const initWebSocket = () => {
                // Connection Successfully
                websocket.onopen = onOpen;
                // Callback when the message is received
                websocket.onmessage = onMessage;
                // Error
                websocket.onerror = onError;
                // Disconnection
                websocket.onclose = onClose;
            };

            onMounted(() => {
                // WebSocket
                if ('WebSocket' in window) {
                    token = Tool.uuid(10);
                    // url：ws://127.0.0.1:8880/ws/xxx
                    websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
                    initWebSocket()

                    // Close
                    // websocket.close();
                } else {
                    alert('This browser is not supported')
                }
            });

            return {
                user
            }
        }
    });
</script>