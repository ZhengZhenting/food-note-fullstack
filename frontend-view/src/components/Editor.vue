<template>
    <div class="editor-container">
        <Toolbar
            class="editor-toolbar"
            :editor="editor"
            :defaultConfig="toolbarConfig"
            :mode="mode"
        />
        <Editor
            class="editor-body"
            :style="{ height: height, overflowY: 'hidden' }"
            v-model="content"
            :defaultConfig="editorConfig"
            :mode="mode"
            @onCreated="onCreated"
        />
    </div>
</template>

<script>
import Vue from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
export default Vue.extend({
    components: { Editor, Toolbar },
    props: {
        receiveContent: { type: String, default: '', required: true },
        height: { type: String, default: 'calc(100vh - 200px)' }
    },
    data() {
        return {
            editor: null,
            content: '<p></p>',
            toolbarConfig: {},
            editorConfig: {
                placeholder: 'Start writing your recipe…',
                MENU_CONF: {
                    uploadImage: {
                        server: 'http://localhost:21090/api/food-share-sys/v1.0/file/upload',
                        fieldName: 'file',
                        maxFileSize: 10 * 1024 * 1024,
                        maxNumberOfFiles: 10,
                        metaWithUrl: false,
                        withCredentials: false,
                        timeout: 10 * 1000,
                        headers: { token: sessionStorage.getItem('token') },
                        customInsert(res, insertFn) { insertFn(res.data, res.data, res.data); },
                    },
                }
            },
            mode: 'default',
        }
    },
    methods: {
        onCreated(editor) {
            this.editor = Object.seal(editor);
            this.toolbarConfig.excludeKeys = ['group-video'];
        },
    },
    watch: {
        receiveContent: {
            handler(v) { this.content = v; },
            deep: true,
            immediate: true
        },
        content(newVal) { this.$emit('on-receive', newVal); },
    },
    beforeDestroy() {
        if (this.editor) this.editor.destroy();
    }
})
</script>

<!-- WangEditor base styles -->
<style src="@wangeditor/editor/dist/css/style.css"></style>

<style>
/* ─── Override WangEditor theme globally ─────────────── */

/* Toolbar background + border */
.editor-toolbar .w-e-toolbar {
    background-color: #fdfaf5 !important;
    border-bottom: 1.5px solid #e8ddd0 !important;
    padding: 4px 8px !important;
}

/* Toolbar button hover */
.editor-toolbar .w-e-toolbar .w-e-menu-item-button:hover,
.editor-toolbar .w-e-toolbar .w-e-menu-item-button:focus {
    background-color: rgba(200, 57, 43, 0.08) !important;
    border-radius: 4px !important;
}

/* Toolbar button active/selected */
.editor-toolbar .w-e-toolbar .w-e-menu-item-button.active {
    color: #c8392b !important;
    background-color: rgba(200, 57, 43, 0.1) !important;
    border-radius: 4px !important;
}

/* Toolbar separator */
.editor-toolbar .w-e-toolbar .w-e-bar-divider {
    background-color: #e8ddd0 !important;
}

/* Editor content area */
.editor-body .w-e-text-container {
    background-color: #ffffff !important;
    font-family: 'DM Sans', 'Klee One', sans-serif !important;
    color: #2a2018 !important;
}

/* Placeholder text */
.editor-body .w-e-text-placeholder {
    color: #c0b09e !important;
    font-family: 'DM Sans', sans-serif !important;
    font-size: 15px !important;
}

/* Editor content typography */
.editor-body .w-e-text-container [data-slate-editor] p {
    color: #2a2018 !important;
    font-family: 'DM Sans', sans-serif !important;
    font-size: 15px !important;
    line-height: 1.8 !important;
}

/* Headings inside editor */
.editor-body .w-e-text-container h1,
.editor-body .w-e-text-container h2,
.editor-body .w-e-text-container h3 {
    color: #2a2018 !important;
    font-family: 'Klee One', cursive !important;
}

/* Blockquote */
.editor-body .w-e-text-container blockquote {
    border-left: 3px solid #c8392b !important;
    background-color: rgba(200, 57, 43, 0.04) !important;
    color: #5a5045 !important;
    padding: 8px 16px !important;
}

/* Dropdown / modal popup background */
.w-e-drop-panel,
.w-e-modal {
    background-color: #fdfaf5 !important;
    border: 1.5px solid #e8ddd0 !important;
    border-radius: 4px !important;
    box-shadow: 0 4px 16px rgba(0,0,0,0.08) !important;
}

/* Dropdown item hover */
.w-e-drop-panel .w-e-panel-content-color li:hover,
.w-e-select-list li:hover {
    background-color: rgba(200, 57, 43, 0.06) !important;
    color: #c8392b !important;
}

/* Link / image modal input */
.w-e-modal input[type="text"],
.w-e-modal input[type="url"] {
    border: 1.5px solid #d6c9b8 !important;
    border-radius: 4px !important;
    font-family: 'DM Sans', sans-serif !important;
    font-size: 14px !important;
    color: #2a2018 !important;
    background-color: #ffffff !important;
    padding: 6px 10px !important;
}

.w-e-modal input:focus {
    outline: none !important;
    border-color: #c8392b !important;
}

/* Modal confirm button */
.w-e-modal button[type="button"] {
    background-color: #c8392b !important;
    border: none !important;
    border-radius: 4px !important;
    color: #fdf8f2 !important;
    font-family: 'Klee One', cursive !important;
    font-size: 13px !important;
    padding: 6px 16px !important;
    cursor: pointer !important;
    transition: background-color 0.15s !important;
}

.w-e-modal button[type="button"]:hover {
    background-color: #b03226 !important;
}
</style>

<style scoped>
.editor-container {
    width: 100%;
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    overflow: hidden;
    background-color: #ffffff;
    transition: border-color 0.2s;
}

.editor-container:focus-within {
    border-color: #c8392b;
}
</style>