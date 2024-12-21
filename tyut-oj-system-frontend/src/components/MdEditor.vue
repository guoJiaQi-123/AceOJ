<template>
  <Editor
    :plugins="plugins"
    :value="value"
    @change="handleChange"
    :mode="mode"
  />
</template>

<script lang="ts" setup>
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor } from "@bytemd/vue-next";
import { defineProps, withDefaults } from "vue";

interface Props {
  value: string;
  handleChange: (v: string) => void;
  mode?: string;
}

const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  mode: () => "split",
  handleChange: (v: string) => {
    console.log(v);
  },
});
const plugins = [gfm(), highlight()];
</script>

<style scoped>
:deep(.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:last-child) {
  display: none;
}
</style>
