package de.dk.bininja.ui.cli;

public abstract class CliCommand<C> {
   protected final String name;

   public CliCommand(String name) {
      this.name = name;
   }

   protected abstract CliCommandResult execute(String input, C controller) throws InterruptedException;
   public abstract void printUsage();

   public String getName() {
      return name;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      CliCommand<?> other = (CliCommand<?>) obj;
      if (this.name == null) {
         if (other.name != null)
            return false;
      } else if (!this.name.equals(other.name))
         return false;
      return true;
   }


}