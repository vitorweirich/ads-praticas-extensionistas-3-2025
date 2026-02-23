<template>
  <div class="container py-5 text-center">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card shadow">
          <div class="card-body p-5">
            <h1 class="display-1 text-danger mb-4">404</h1>
            <h2 class="mb-4">Página não encontrada</h2>
            <p class="lead mb-4">
              A página que você está procurando não existe ou foi removida.
            </p>
            <router-link to="/" class="btn btn-primary btn-lg">
              <i class="fas fa-home me-2"></i> Voltar para a página inicial
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
